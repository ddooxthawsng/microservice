import axiosInstance from '../utils/axiosConfig';

export interface AuditLog {
    id: string;
    serviceName: string;
    userId: string;
    username: string;
    action: string;
    details: string;
    ipAddress: string;
    status: string;
    timestamp: string;
}

export const auditService = {
    getLogs: async (): Promise<AuditLog[]> => {
        const response = await axiosInstance.get('/audit-logs');
        return response.data.result;
    }
};
