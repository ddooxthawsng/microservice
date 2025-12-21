import React, { useState, useEffect } from 'react';
import { Table, Button, Input, message, Tag, Space } from 'antd';
import { ReloadOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import { auditService } from '../../services/auditService';
import type { AuditLog } from '../../services/auditService';

const AuditLogPage: React.FC = () => {
    const [logs, setLogs] = useState<AuditLog[]>([]);
    const [loading, setLoading] = useState(false);
    const [searchText, setSearchText] = useState('');

    const fetchLogs = async () => {
        setLoading(true);
        try {
            const data = await auditService.getLogs();
            setLogs(data);
        } catch (error) {
            message.error('Failed to load audit logs');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchLogs();
    }, []);

    const filteredLogs = logs.filter(log =>
        log.action?.toLowerCase().includes(searchText.toLowerCase()) ||
        log.username?.toLowerCase().includes(searchText.toLowerCase()) ||
        log.serviceName?.toLowerCase().includes(searchText.toLowerCase())
    );

    const columns = [
        {
            title: 'Time',
            dataIndex: 'timestamp',
            key: 'timestamp',
            render: (text: string) => dayjs(text).format('YYYY-MM-DD HH:mm:ss'),
            sorter: (a: AuditLog, b: AuditLog) => dayjs(a.timestamp).unix() - dayjs(b.timestamp).unix(),
            defaultSortOrder: 'descend' as const,
        },
        {
            title: 'Service',
            dataIndex: 'serviceName',
            key: 'serviceName',
            render: (text: string) => <Tag color="blue">{text}</Tag>
        },
        {
            title: 'User',
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: 'Action',
            dataIndex: 'action',
            key: 'action',
            render: (text: string) => <Tag color="green">{text}</Tag>
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (text: string) => (
                <Tag color={text === 'SUCCESS' ? 'success' : 'error'}>
                    {text}
                </Tag>
            )
        },
        {
            title: 'IP Address',
            dataIndex: 'ipAddress',
            key: 'ipAddress',
        },
        {
            title: 'Details',
            dataIndex: 'details',
            key: 'details',
            ellipsis: true,
        },
    ];

    return (
        <div>
            <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h2>System Audit Logs</h2>
                <Space>
                    <Input.Search
                        placeholder="Search logs..."
                        onSearch={value => setSearchText(value)}
                        onChange={e => setSearchText(e.target.value)}
                        style={{ width: 300 }}
                    />
                    <Button icon={<ReloadOutlined />} onClick={fetchLogs}>Refresh</Button>
                </Space>
            </div>

            <Table
                columns={columns}
                dataSource={filteredLogs}
                rowKey="id"
                loading={loading}
                pagination={{ pageSize: 20 }}
                expandable={{
                    expandedRowRender: record => <p style={{ margin: 0 }}>{record.details}</p>,
                }}
            />
        </div>
    );
};

export default AuditLogPage;
