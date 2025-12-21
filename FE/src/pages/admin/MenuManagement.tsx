import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, Switch, message, Space, TreeSelect, Popconfirm } from 'antd';
import { EditOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons';
import { menuService } from '../../services/menuService';
import type { Menu } from '../../types';

const MenuManagement: React.FC = () => {
    const [menus, setMenus] = useState<Menu[]>([]);
    const [loading, setLoading] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [editingMenu, setEditingMenu] = useState<Menu | null>(null);
    const [form] = Form.useForm();

    const fetchMenus = async () => {
        setLoading(true);
        try {
            // Use getTree for the table display to show hierarchy
            const data = await menuService.getTree();
            setMenus(data);
        } catch (error) {
            message.error('Failed to load menus');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchMenus();
    }, []);

    const handleAdd = () => {
        setEditingMenu(null);
        form.resetFields();
        setModalVisible(true);
    };

    const handleEdit = (menu: Menu) => {
        setEditingMenu(menu);
        form.setFieldsValue({
            ...menu,
        });
        setModalVisible(true);
    };

    const handleDelete = async (id: string) => {
        try {
            await menuService.deleteMenu(id);
            message.success('Menu deleted successfully');
            fetchMenus();
        } catch (error) {
            message.error('Failed to delete menu');
        }
    };

    const handleOk = async () => {
        try {
            const values = await form.validateFields();
            if (editingMenu) {
                await menuService.updateMenu(editingMenu.id, values);
                message.success('Menu updated successfully');
            } else {
                await menuService.createMenu(values);
                message.success('Menu created successfully');
            }
            setModalVisible(false);
            fetchMenus();
        } catch (error) {
            message.error('Operation failed');
        }
    };

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Path',
            dataIndex: 'path',
            key: 'path',
        },
        {
            title: 'Icon',
            dataIndex: 'icon',
            key: 'icon',
        },
        {
            title: 'Sort Order',
            dataIndex: 'sortOrder',
            key: 'sortOrder',
        },
        {
            title: 'Active',
            dataIndex: 'isActive',
            key: 'isActive',
            render: (isActive: boolean) => (
                <Switch checked={isActive} disabled />
            )
        },
        {
            title: 'Action',
            key: 'action',
            render: (_: any, record: Menu) => (
                <Space size="middle">
                    <Button icon={<EditOutlined />} onClick={() => handleEdit(record)} />
                    <Popconfirm
                        title="Delete Menu"
                        description="Are you sure to delete this menu?"
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

    // Convert menus to TreeSelect data
    const mapToTreeData = (menuList: Menu[]): any[] => {
        return menuList.map(item => ({
            value: item.id,
            title: item.name,
            children: item.children ? mapToTreeData(item.children) : [],
        }));
    };

    return (
        <div>
            <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h2>Menu Management</h2>
                <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
                    Add Menu
                </Button>
            </div>
            <Table
                columns={columns}
                dataSource={menus}
                rowKey="id"
                loading={loading}
                expandable={{ defaultExpandAllRows: true }}
            />

            <Modal
                title={editingMenu ? "Edit Menu" : "Add Menu"}
                open={modalVisible}
                onOk={handleOk}
                onCancel={() => setModalVisible(false)}
            >
                <Form form={form} layout="vertical">
                    <Form.Item
                        name="name"
                        label="Name"
                        rules={[{ required: true, message: 'Please input menu name!' }]}
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item name="description" label="Description">
                        <Input />
                    </Form.Item>
                    <Form.Item name="path" label="Path">
                        <Input />
                    </Form.Item>
                    <Form.Item name="icon" label="Icon">
                        <Input />
                    </Form.Item>
                    <Form.Item name="parentId" label="Parent Menu">
                        <TreeSelect
                            style={{ width: '100%' }}
                            dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                            treeData={mapToTreeData(menus)}
                            placeholder="Select parent menu"
                            allowClear
                            treeDefaultExpandAll
                        />
                    </Form.Item>
                    <Form.Item name="sortOrder" label="Sort Order">
                        <InputNumber style={{ width: '100%' }} />
                    </Form.Item>
                    <Form.Item name="isActive" label="Active" valuePropName="checked">
                        <Switch />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};

export default MenuManagement;
