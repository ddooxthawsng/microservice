import api from '../utils/axiosConfig';
import { message, notification } from 'antd';

// Auth API functions
export const authAPI = {
  // Đăng nhập sử dụng endpoint /auth/login
  login: async (credentials) => {
    try {
      // Gọi API /auth/login thay vì /users/login
      const response = await api.post('/auth/login', credentials);
      
      if (response.data.code === 0 && response.data.result.authenticated) {
        // API trả về code: 0 khi thành công và token thật
        const authResponse = {
          data: {
            code: 200, // Normalize về 200 cho internal logic
            message: response.data.message || 'Đăng nhập thành công',
            result: {
              token: response.data.result.token, // Token thật từ API
              user: response.data.result.user || { username: credentials.username } // Fallback user info nếu không có
            }
          }
        };
        
        console.log('API login response:', authResponse);
        
        return authResponse;
      } else {
        throw new Error(response.data.message || 'Đăng nhập thất bại');
      }
    } catch (error) {
      // Xử lý lỗi đăng nhập với thông báo chi tiết
      let errorMessage = 'Đăng nhập thất bại!';
      
      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.message) {
        errorMessage = error.message;
      }
      
      // Error notification sẽ được handle ở Login component
      
      throw error;
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    
    // Thông báo đăng xuất thành công
    message.success('Đăng xuất thành công!');
  },

  getCurrentUser: () => {
    return JSON.parse(localStorage.getItem('user') || 'null');
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  }
};

// User API functions with improved error handling
export const userAPI = {
  getUsers: async () => {
    try {
      const response = await api.get('/users');
      return response;
    } catch (error) {
      notification.error({
        message: 'Lỗi tải danh sách',
        description: 'Không thể tải danh sách người dùng!',
        duration: 4,
      });
      throw error;
    }
  },
  
  getUser: async (id) => {
    try {
      const response = await api.get(`/users/${id}`);
      return response;
    } catch (error) {
      message.error('Không thể tải thông tin người dùng!');
      throw error;
    }
  },
  
  createUser: async (userData) => {
    try {
      const response = await api.post('/users', userData);
      
      // Không hiển thị notification ở đây nữa, sẽ handle ở component
      return response;
    } catch (error) {
      // Error message sẽ được handle bởi axios interceptor
      throw error;
    }
  },
  
  updateUser: async (id, userData) => {
    try {
      const response = await api.put(`/users/${id}`, userData);
      
      notification.success({
        message: 'Cập nhật thành công!',
        description: `Đã cập nhật thông tin cho ${userData.firstName} ${userData.lastName}`,
        duration: 4,
      });
      
      return response;
    } catch (error) {
      throw error;
    }
  },
  
  deleteUser: async (id) => {
    try {
      const response = await api.delete(`/users/${id}`);
      
      message.success('Xóa người dùng thành công!');
      
      return response;
    } catch (error) {
      message.error('Không thể xóa người dùng!');
      throw error;
    }
  }
};

export default api;