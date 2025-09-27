import React, { createContext, useContext, useState, useEffect } from 'react';
import { authAPI } from '../services/api';
import { message, notification } from 'antd';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Kiểm tra xem user đã đăng nhập chưa khi app khởi động
    const token = localStorage.getItem('token');
    const savedUser = authAPI.getCurrentUser();
    
    if (token && savedUser) {
      setUser(savedUser);
    }
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    try {
      setLoading(true);
      const response = await authAPI.login(credentials);
      
      if (response.data.code === 200) {
        const token = response.data.result.token;
        const userData = response.data.result.user;
        
        // Lưu token và user info vào localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(userData));
        
        setUser(userData);
        
        console.log('Login successful, redirecting...', { token, userData });
        return { success: true };
      } else {
        throw new Error(response.data.message || 'Đăng nhập thất bại');
      }
    } catch (error) {
      console.error('AuthContext login error:', error);
      const errorMessage = error.response?.data?.message || error.message || 'Đăng nhập thất bại';
      
      // Hiển thị notification lỗi
      notification.error({
        message: '❌ Đăng nhập thất bại!',
        description: errorMessage,
        duration: 4,
        placement: 'topRight',
      });
      
      return { success: false, error: errorMessage };
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    authAPI.logout();
    setUser(null);
  };

  const value = {
    user,
    login,
    logout,
    loading,
    isAuthenticated: authAPI.isAuthenticated()
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
