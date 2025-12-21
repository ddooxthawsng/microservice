export interface Permission {
    name: string;
    description: string;
}

export interface Role {
    name: string;
    description: string;
    permissions: Permission[];
}

export interface User {
    id: string;
    username: string;
    firstName: string;
    lastName: string;
    dob: string;
    roles: Role[];
}

export interface Menu {
    id: string;
    name: string;
    description: string;
    path: string;
    icon: string;
    parentId: string | null;
    sortOrder: number;
    isActive: boolean;
    children?: Menu[];
}

export interface LoginResponse {
    token: string;
    authenticated: boolean;
}

export interface ApiResponse<T> {
    code: number;
    message?: string;
    result: T;
}

export interface AuthState {
    isAuthenticated: boolean;
    user: User | null;
    token: string | null;
    isLoading: boolean;
}
