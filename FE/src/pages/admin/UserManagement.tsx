import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, DatePicker, message, Space, Select, Tag, Popconfirm } from 'antd';
import { EditOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import { userService } from '../../services/userService';
import { roleService } from '../../services/roleService';
import type { User, Role } from '../../types';

const UserManagement: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [roles, setRoles] = useState<Role[]>([]);
    const [loading, setLoading] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [editingUser, setEditingUser] = useState<User | null>(null);
    const [form] = Form.useForm();

    const fetchUsers = async () => {
        setLoading(true);
        try {
            const data = await userService.getUsers();
            setUsers(data);
        } catch (error) {
            message.error('Failed to load users');
        } finally {
            setLoading(false);
        }
    };

    const fetchRoles = async () => {
        try {
            const data = await roleService.getRoles();
            setRoles(data);
        } catch (error) {
            console.error("Failed to load roles");
        }
    };

    useEffect(() => {
        fetchUsers();
        fetchRoles();
    }, []);

    const handleAdd = () => {
        setEditingUser(null);
        form.resetFields();
        setModalVisible(true);
    };

    const handleEdit = (user: User) => {
        setEditingUser(user);
        form.setFieldsValue({
            ...user,
            dob: user.dob ? dayjs(user.dob) : null,
            roles: user.roles.map(r => r.name) // Assuming selecting roles by name
        });
        setModalVisible(true);
    };

    const handleDelete = async (id: string) => {
        try {
            await userService.deleteUser(id);
            message.success('User deleted successfully');
            fetchUsers();
        } catch (error) {
            console.error('Failed to delete user', error);
        }
    };

    const handleOk = async () => {
        try {
            const values = await form.validateFields();
            const roleNames = values.roles || [];

            if (editingUser) {
                const payload = {
                    ...values,
                    dob: values.dob ? values.dob.format('YYYY-MM-DD') : undefined,
                    roles: roleNames
                };
                await userService.updateUser(editingUser.id, payload);
                message.success('User updated successfully');
            } else {
                // Creation request does not support roles, so we create then update
                const createPayload = {
                    username: values.username,
                    password: values.password,
                    firstName: values.firstName,
                    lastName: values.lastName,
                    dob: values.dob ? values.dob.format('YYYY-MM-DD') : undefined,
                };

                const newUser = await userService.createUser(createPayload);

                if (roleNames.length > 0) {
                    await userService.updateUser(newUser.id, { roles: roleNames });
                }

                message.success('User created successfully');
            }
            setModalVisible(false);
            fetchUsers();
        } catch (error) {
            console.error('Operation failed', error);
        }
    };

    const columns = [
        {
            title: 'Username',
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: 'First Name',
            dataIndex: 'firstName',
            key: 'firstName',
        },
        {
            title: 'Last Name',
            dataIndex: 'lastName',
            key: 'lastName',
        },
        {
            title: 'Date of Birth',
            dataIndex: 'dob',
            key: 'dob',
        },
        {
            title: 'Roles',
            dataIndex: 'roles',
            key: 'roles',
            render: (userRoles: Role[]) => (
                <>
                    {userRoles.map(role => (
                        <Tag key={role.name} color="blue">
                            {role.name}
                        </Tag>
                    ))}
                </>
            ),
        },
        {
            title: 'Action',
            key: 'action',
            render: (_: any, record: User) => (
                <Space size="middle">
                    <Button icon={<EditOutlined />} onClick={() => handleEdit(record)} />
                    <Popconfirm
                        title="Delete User"
                        description="Are you sure to delete this user?"
                        onConfirm={() => handleDelete(record.id)}
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
                <h2>User Management</h2>
                <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
                    Add User
                </Button>
            </div>
            <Table columns={columns} dataSource={users} rowKey="id" loading={loading} />

            <Modal
                title={editingUser ? "Edit User" : "Add User"}
                open={modalVisible}
                onOk={handleOk}
                onCancel={() => setModalVisible(false)}
            >
                <Form form={form} layout="vertical">
                    <Form.Item
                        name="username"
                        label="Username"
                        rules={[{ required: true, message: 'Please input username!' }]}
                    >
                        <Input />
                    </Form.Item>
                    {!editingUser && (
                        <Form.Item
                            name="password"
                            label="Password"
                            rules={[{ required: true, message: 'Please input password!' }]}
                        >
                            <Input.Password />
                        </Form.Item>
                    )}
                    <Form.Item name="firstName" label="First Name">
                        <Input />
                    </Form.Item>
                    <Form.Item name="lastName" label="Last Name">
                        <Input />
                    </Form.Item>
                    <Form.Item name="dob" label="Date of Birth">
                        <DatePicker style={{ width: '100%' }} />
                    </Form.Item>
                    <Form.Item name="roles" label="Roles">
                        <Select mode="multiple">
                            {roles.map(role => (
                                <Select.Option key={role.name} value={role.name}>{role.name}</Select.Option>
                            ))}
                        </Select>
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default UserManagement;
