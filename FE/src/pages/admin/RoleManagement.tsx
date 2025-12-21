import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, message, Space, Tag, Popconfirm } from 'antd';
import { EditOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import { roleService } from '../../services/roleService';
import type { Role, Permission } from '../../types';

const RoleManagement: React.FC = () => {
    const [roles, setRoles] = useState<Role[]>([]);
    const [permissions, setPermissions] = useState<Permission[]>([]);
    const [loading, setLoading] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [editingRole, setEditingRole] = useState<Role | null>(null);
    const [form] = Form.useForm();

    // New states for Permission UI
    const [selectedPermissionNames, setSelectedPermissionNames] = useState<React.Key[]>([]);
    const [searchText, setSearchText] = useState('');

    const fetchRoles = async () => {
        setLoading(true);
        try {
            const data = await roleService.getRoles();
            setRoles(data);
        } catch (error) {
            message.error('Failed to load roles');
        } finally {
            setLoading(false);
        }
    };

    const fetchPermissions = async () => {
        try {
            const data = await roleService.getPermissions();
            setPermissions(data);
        } catch (error) {
            console.error("Failed to load permissions");
        }
    };

    useEffect(() => {
        fetchRoles();
        fetchPermissions();
    }, []);

    const handleAdd = () => {
        setEditingRole(null);
        form.resetFields();
        setSelectedPermissionNames([]); // Reset selection
        setSearchText('');
        setModalVisible(true);
    };

    const handleEdit = (role: Role) => {
        setEditingRole(role);
        form.setFieldsValue({
            ...role
        });
        // Pre-select permissions
        const currentPermNames = role.permissions.map(p => p.name);
        setSelectedPermissionNames(currentPermNames);
        setSearchText('');
        setModalVisible(true);
    };

    const handleDelete = async (name: string) => {
        try {
            await roleService.deleteRole(name);
            message.success('Role deleted successfully');
            fetchRoles();
        } catch (error) {
            message.error('Failed to delete role');
        }
    };

    const handleOk = async () => {
        try {
            const values = await form.validateFields();
            const payload = {
                ...values,
                permissions: selectedPermissionNames // Use the state from Table selection
            };

            if (editingRole) {
                await roleService.updateRole(editingRole.name, payload);
                message.success('Role updated successfully');
            } else {
                await roleService.createRole(payload);
                message.success('Role created successfully');
            }
            setModalVisible(false);
            fetchRoles();
        } catch (error) {
            message.error('Operation failed');
        }
    };

    // Filter permissions based on search
    const filteredPermissions = permissions.filter(p =>
        p.name.toLowerCase().includes(searchText.toLowerCase()) ||
        p.description.toLowerCase().includes(searchText.toLowerCase())
    );

    const permissionColumns = [
        {
            title: 'Permission Code',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        }
    ];

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Permissions',
            dataIndex: 'permissions',
            key: 'permissions',
            render: (perms: Permission[]) => (
                <>
                    {perms.map(p => (
                        <Tag key={p.name}>{p.name}</Tag>
                    ))}
                </>
            ),
        },
        {
            title: 'Action',
            key: 'action',
            render: (_: any, record: Role) => (
                <Space size="middle">
                    <Button icon={<EditOutlined />} onClick={() => handleEdit(record)} />
                    <Popconfirm
                        title="Delete Role"
                        description="Are you sure to delete this role?"
                        onConfirm={() => handleDelete(record.name)}
                        okText="Yes"
                        cancelText="No"
                    >
                        <Button icon={<DeleteOutlined />} danger />
                    </Popconfirm>
                </Space>
            ),
        },
    ];

    return (
        <div>
            <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h2>Role Management</h2>
                <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
                    Add Role
                </Button>
            </div>
            <Table columns={columns} dataSource={roles} rowKey="name" loading={loading} />

            <Modal
                title={editingRole ? "Edit Role" : "Add Role"}
                open={modalVisible}
                onOk={handleOk}
                onCancel={() => setModalVisible(false)}
                width={700}
            >
                <Form form={form} layout="vertical">
                    <Form.Item
                        name="name"
                        label="Name"
                        rules={[{ required: true, message: 'Please input role name!' }]}
                    >
                        <Input disabled={!!editingRole} />
                    </Form.Item>
                    <Form.Item name="description" label="Description">
                        <Input.TextArea />
                    </Form.Item>

                    <Form.Item label="Permissions">
                        <Input.Search
                            placeholder="Search permissions..."
                            style={{ marginBottom: 8 }}
                            onChange={e => setSearchText(e.target.value)}
                        />
                        <Table
                            size="small"
                            rowSelection={{
                                type: 'checkbox',
                                selectedRowKeys: selectedPermissionNames,
                                onChange: (selectedRowKeys) => setSelectedPermissionNames(selectedRowKeys),
                            }}
                            columns={permissionColumns}
                            dataSource={filteredPermissions}
                            rowKey="name"
                            pagination={{ pageSize: 5 }}
                            scroll={{ y: 240 }}
                            bordered
                        />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default RoleManagement;
