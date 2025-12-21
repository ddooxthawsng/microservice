import React, { createContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import type { AuthState, LoginResponse } from '../types';
import { authService } from '../services/authService';
import { setAuthToken } from '../utils/axiosConfig';
import jwtDecode from 'jwt-decode';

interface AuthContextType extends AuthState {
    login: (username: string, password: string) => Promise<void>;
    logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [state, setState] = useState<AuthState>({
        isAuthenticated: false,
        user: null,
        token: localStorage.getItem('token'),
        isLoading: true,
    });

    useEffect(() => {
        const initAuth = async () => {
            const token = localStorage.getItem('token');
            if (token) {
                try {
                    setAuthToken(token);
                    const decoded: any = jwtDecode(token);
                    const currentTime = Date.now() / 1000;

                    if (decoded.exp < currentTime) {
                        // Token expired
                        handleLogout();
                    } else {
                        // Token valid, fetch user info
                        try {
                            const user = await authService.getCurrentUser();
                            setState({
                                isAuthenticated: true,
                                user,
                                token,
                                isLoading: false,
                            });
                        } catch (err: any) {
                            console.error("Failed to fetch user info", err);
                            // Only logout if 401 Unauthorized
                            if (err.response && err.response.status === 401) {
                                handleLogout();
                            } else {
                                // For other errors (500, network), assume token is still valid
                                // We trust local token since it passed expiry check
                                setState({
                                    isAuthenticated: true,
                                    user: null,
                                    token,
                                    isLoading: false,
                                });
                            }
                        }
                    }
                } catch (error) {
                    console.error("Invalid token", error);
                    handleLogout();
                }
            } else {
                setState(prev => ({ ...prev, isLoading: false }));
            }
        };

        initAuth();
    }, []);

    const login = async (username: string, password: string) => {
        try {
            const loginData: LoginResponse = await authService.login(username, password);
            if (loginData.token) {
                // Thử lấy thông tin user, nếu lỗi thì vẫn tính là đã login với token
                let user = null;
                try {
                    user = await authService.getCurrentUser();
                } catch (userError) {
                    console.warn("Could not fetch user info after login", userError);
                }

                setState({
                    isAuthenticated: true,
                    user,
                    token: loginData.token,
                    isLoading: false
                });
            }
        } catch (error) {
            console.error("Login failed", error);
            throw error;
        }
    };

    const logout = async () => {
        if (state.token) {
            await authService.logout(state.token);
        }
        handleLogout();
    };

    const handleLogout = () => {
        setAuthToken(null);
        setState({
            isAuthenticated: false,
            user: null,
            token: null,
            isLoading: false
        });
    };

    return (
        <AuthContext.Provider value={{ ...state, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
