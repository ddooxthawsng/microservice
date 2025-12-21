import axiosInstance from '../utils/axiosConfig';
import type { Menu, ApiResponse } from '../types';

export const menuService = {
    getTree: async (): Promise<Menu[]> => {
        const response = await axiosInstance.get<{ result: Menu[] }>('/menus/tree');
        return response.data.result;
    },

    getMenus: async (): Promise<Menu[]> => {
        const response = await axiosInstance.get<ApiResponse<Menu[]>>('/menus');
        return response.data.result;
    },

    createMenu: async (menu: Partial<Menu>): Promise<Menu> => {
        const response = await axiosInstance.post<ApiResponse<Menu>>('/menus', menu);
        return response.data.result;
    },

    updateMenu: async (id: string, menu: Partial<Menu>): Promise<Menu> => {
        const response = await axiosInstance.put<ApiResponse<Menu>>(`/menus/${id}`, menu);
        return response.data.result;
    },

    deleteMenu: async (id: string): Promise<void> => {
        await axiosInstance.delete(`/menus/${id}`);
    }
};
