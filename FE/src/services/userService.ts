import axiosInstance from '../utils/axiosConfig';
import type { ApiResponse, User } from '../types';

export const userService = {
    getUsers: async (): Promise<User[]> => {
        const response = await axiosInstance.get<ApiResponse<User[]>>('/users');
        return response.data.result;
    },

    createUser: async (user: Partial<User>): Promise<User> => {
        const response = await axiosInstance.post<ApiResponse<User>>('/users', user);
        return response.data.result;
    },

    updateUser: async (id: string, user: Partial<User>): Promise<User> => {
        const response = await axiosInstance.put<ApiResponse<User>>(`/users/${id}`, user);
        return response.data.result;
    },

    deleteUser: async (id: string): Promise<void> => {
        await axiosInstance.delete(`/users/${id}`);
    }
};
