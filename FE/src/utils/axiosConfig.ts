import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // API Gateway URL
    headers: {
        'Content-Type': 'application/json',
    },
});

export const setAuthToken = (token: string | null) => {
    if (token) {
        axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        localStorage.setItem('token', token);
    } else {
        delete axiosInstance.defaults.headers.common['Authorization'];
        localStorage.removeItem('token');
    }
};

import { message } from 'antd';

axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            const { status, data } = error.response;

            // Handle ApiResponse structure (Global Exception Handler from BE)
            if (data && data.code && data.message) {
                message.error(data.message);
            }

            if (status === 401) {
                // Retrieve the original request's URL to avoid redirect loops if checking auth
                const originalRequestUrl = error.config.url;
                if (!originalRequestUrl?.includes('/auth/token')) {
                    // Clear token and potentially redirect to login
                    setAuthToken(null);
                    window.location.href = '/login';
                }
            }
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;
