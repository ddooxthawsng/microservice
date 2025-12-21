import axiosInstance, { setAuthToken } from '../utils/axiosConfig';
import type { ApiResponse, LoginResponse, User } from '../types';

export const authService = {
    login: async (username: string, password: string): Promise<LoginResponse> => {
        const response = await axiosInstance.post<ApiResponse<LoginResponse>>('/auth/token', {
            username,
            password
        });

        const loginData = response.data.result;

        if (loginData && loginData.token) {
            setAuthToken(loginData.token);
        }

        return loginData;
    },

    logout: async (token: string) => {
        try {
            await axiosInstance.post('/auth/logout', { token });
        } catch (error) {
            console.error("Logout failed", error);
        } finally {
            setAuthToken(null);
        }
    },

    getCurrentUser: async (): Promise<User> => {
        const response = await axiosInstance.get<ApiResponse<User>>('/users/myInfo');
        return response.data.result;
    }
};
