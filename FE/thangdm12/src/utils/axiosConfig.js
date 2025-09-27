import axios from 'axios';
import { message, notification } from 'antd';

// Base API configuration
const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 seconds timeout
});

// Request interceptor để thêm token vào header
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // Log request cho debugging
    console.log(`🚀 Request: ${config.method?.toUpperCase()} ${config.url}`, {
      data: config.data,
      headers: config.headers
    });
    
    return config;
  },
  (error) => {
    console.error('❌ Request Error:', error);
    message.error('Lỗi khi gửi yêu cầu!');
    return Promise.reject(error);
  }
);

// Response interceptor để xử lý response và lỗi
api.interceptors.response.use(
  (response) => {
    // Log successful response
    console.log(`✅ Response: ${response.status}`, response.data);
    
    // Kiểm tra response structure
    if (response.data && typeof response.data === 'object') {
      // Nếu có code trong response data
      if (response.data.code && response.data.code !== 200) {
        const errorMessage = response.data.message || 'Có lỗi xảy ra!';
        message.error(errorMessage);
        return Promise.reject(new Error(errorMessage));
      }
      
      // Hiển thị message thành công nếu có
      if (response.data.message && response.config.method !== 'get') {
        message.success(response.data.message);
      }
    }
    
    return response;
  },
  (error) => {
    console.error('❌ Response Error:', error);
    
    let errorMessage = 'Có lỗi xảy ra!';
    let errorTitle = 'Lỗi';
    
    if (error.response) {
      // Server responded with error status
      const { status, data } = error.response;
      
      switch (status) {
        case 400:
          errorTitle = 'Yêu cầu không hợp lệ';
          errorMessage = data?.message || 'Dữ liệu gửi lên không đúng định dạng!';
          break;
        case 401:
          errorTitle = 'Không có quyền truy cập';
          errorMessage = data?.message || 'Vui lòng đăng nhập lại!';
          // Auto logout on 401
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          if (window.location.pathname !== '/login') {
            window.location.href = '/login';
          }
          break;
        case 403:
          errorTitle = 'Bị cấm truy cập';
          errorMessage = data?.message || 'Bạn không có quyền thực hiện hành động này!';
          break;
        case 404:
          errorTitle = 'Không tìm thấy';
          errorMessage = data?.message || 'Không tìm thấy tài nguyên yêu cầu!';
          break;
        case 500:
          errorTitle = 'Lỗi server';
          errorMessage = data?.message || 'Lỗi server nội bộ, vui lòng thử lại sau!';
          break;
        default:
          errorMessage = data?.message || `Lỗi ${status}: ${error.response.statusText}`;
      }
      
      // Hiển thị notification cho tất cả lỗi
      notification.error({
        message: errorTitle,
        description: errorMessage,
        duration: status >= 500 ? 6 : 4,
        placement: 'topRight',
      });
      
    } else if (error.request) {
      // Network error
      errorTitle = 'Lỗi kết nối';
      errorMessage = 'Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng!';
      
      notification.error({
        message: errorTitle,
        description: errorMessage,
        duration: 8,
      });
      
    } else {
      // Something else happened
      errorMessage = error.message || 'Có lỗi không xác định xảy ra!';
      message.error(errorMessage);
    }
    
    return Promise.reject(error);
  }
);

export default api;
