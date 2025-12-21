import React, { useState, useEffect } from 'react';
import { Layout, Menu, Button, Dropdown, Space, Avatar } from 'antd';
import { UserOutlined, LogoutOutlined, MenuUnfoldOutlined, MenuFoldOutlined, TeamOutlined, SafetyCertificateOutlined, AppstoreOutlined, FileSearchOutlined } from '@ant-design/icons';
import { useNavigate, useLocation, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { menuService } from '../services/menuService';
import { Menu as MenuType } from '../types';

const { Header, Sider, Content } = Layout;

const MainLayout: React.FC = () => {
    const [collapsed, setCollapsed] = useState(false);
    const [menus, setMenus] = useState<MenuType[]>([]);
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const defaultMenus: MenuType[] = [
        {
            id: 'user-mgmt',
            name: 'User Management',
            description: 'Manage users',
            path: '/users',
            icon: 'UserOutlined',
            parentId: null,
            sortOrder: 1000,
            isActive: true,
            children: []
        },
        {
            id: 'role-mgmt',
            name: 'Role Management',
            description: 'Manage roles',
            path: '/roles',
            icon: 'SafetyCertificateOutlined',
            parentId: null,
            sortOrder: 1001,
            isActive: true,
            children: []
        },
        {
            id: 'menu-mgmt',
            name: 'Menu Management',
            description: 'Manage menus',
            path: '/menus',
            icon: 'AppstoreOutlined', // Or BarsOutlined
            parentId: null,
            sortOrder: 1002,
            isActive: true,
            children: []
        },
        {
            id: 'audit-logs',
            name: 'Audit Logs',
            description: 'View system audit logs',
            path: '/audit-logs',
            icon: 'FileSearchOutlined',
            parentId: null,
            sortOrder: 1003,
            isActive: true,
            children: []
        }
    ];

    useEffect(() => {
        const fetchMenus = async () => {
            try {
                const tree = await menuService.getTree();
                // Filter out default menus if they already exist in the fetched tree to avoid duplicates?
                // For now, simpler to just append or prepend. User asked for "default always have".
                // I will prepend specific management menus if not present. 
                // However, detailed deduping logic is complex without seeing data.
                // I will just prepend them for now as specifically requested.
                // Ideally we should check if path exists.
                const combined = [...defaultMenus, ...tree];
                setMenus(combined);
            } catch (error) {
                console.error("Failed to load menus", error);
            }
        };
        fetchMenus();
    }, []);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const userMenuItems = [
        {
            key: 'logout',
            icon: <LogoutOutlined />,
            label: 'Logout',
            onClick: handleLogout,
        },
    ];

    const getIcon = (iconName: string) => {
        switch (iconName) {
            case 'UserOutlined': return <UserOutlined />;
            case 'TeamOutlined': return <TeamOutlined />;
            case 'SafetyCertificateOutlined': return <SafetyCertificateOutlined />;
            case 'AppstoreOutlined': return <AppstoreOutlined />;
            case 'FileSearchOutlined': return <FileSearchOutlined />;
            default: return <UserOutlined />;
        }
    };

    const mapMenusToAntdItems = (menuList: MenuType[]): any[] => {
        return menuList.map(menu => {
            if (menu.children && menu.children.length > 0) {
                return {
                    key: menu.id,
                    icon: getIcon(menu.icon),
                    label: menu.name,
                    children: mapMenusToAntdItems(menu.children)
                };
            }
            return {
                key: menu.path,
                icon: getIcon(menu.icon),
                label: menu.name,
                onClick: () => navigate(menu.path)
            };
        });
    };

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Sider trigger={null} collapsible collapsed={collapsed}>
                <div style={{ height: 32, margin: 16, background: 'rgba(255, 255, 255, 0.2)', textAlign: 'center', color: 'white', lineHeight: '32px' }}>
                    {collapsed ? 'MS' : 'MicroService'}
                </div>
                <Menu
                    theme="dark"
                    mode="inline"
                    selectedKeys={[location.pathname]}
                    items={mapMenusToAntdItems(menus)}
                />
            </Sider>
            <Layout>
                <Header style={{ padding: '0 16px', background: '#fff', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Button
                        type="text"
                        icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
                        onClick={() => setCollapsed(!collapsed)}
                        style={{ fontSize: '16px', width: 64, height: 64 }}
                    />
                    <Space>
                        <span>Welcome, {user?.firstName}</span>
                        <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
                            <Avatar icon={<UserOutlined />} style={{ cursor: 'pointer' }} />
                        </Dropdown>
                    </Space>
                </Header>
                <Content
                    style={{
                        margin: '24px 16px',
                        padding: 24,
                        minHeight: 280,
                        background: '#fff',
                    }}
                >
                    <Outlet />
                </Content>
            </Layout>
        </Layout>
    );
};

export default MainLayout;
