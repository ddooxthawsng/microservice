import axiosInstance from '../utils/axiosConfig';
import type { ApiResponse, Role, Permission } from '../types';

export const roleService = {
    getPermissions: async (): Promise<Permission[]> => {
        const response = await axiosInstance.get<ApiResponse<Permission[]>>('/account-service/permissions');
        return response.data.result;
    },
    getRoles: async (): Promise<Role[]> => {
        const response = await axiosInstance.get<ApiResponse<Role[]>>('/account-service/roles');
        return response.data.result;
    },

    createRole: async (role: Partial<Role>): Promise<Role> => {
        const response = await axiosInstance.post<ApiResponse<Role>>('/account-service/roles', role);
        return response.data.result;
    },

    updateRole: async (_name: string, role: Partial<Role>): Promise<Role> => {
        const response = await axiosInstance.post<ApiResponse<Role>>('/account-service/roles', role);
        return response.data.result;
    },

    deleteRole: async (name: string): Promise<void> => {
        await axiosInstance.delete(`/account-service/roles/${name}`);
    }
};
