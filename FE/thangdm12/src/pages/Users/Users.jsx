import React, { useState, useEffect } from 'react';
import {
  Table,
  Card,
  Button,
  Space,
  Modal,
  Form,
  Input,
  DatePicker,
  Popconfirm,
  message,
  Typography,
  Tag,
  Tooltip,
  Row,
  Col
} from 'antd';
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  SearchOutlined,
  ReloadOutlined,
  UserOutlined
} from '@ant-design/icons';
import { userAPI } from '../../services/api';
import dayjs from 'dayjs';

const { Title, Text } = Typography;
const { Search } = Input;

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [searchText, setSearchText] = useState('');
  const [form] = Form.useForm();

  // Mock data cho demo
  const mockUsers = [
    {
      id: '1',
      username: 'admin',
      firstName: 'Admin',
      lastName: 'User',
      dob: '1990-01-01',
      createdDate: '2024-01-01',
      status: 'active'
    },
    {
      id: '2',
      username: 'johndoe',
      firstName: 'John',
      lastName: 'Doe',
      dob: '1985-05-15',
      createdDate: '2024-01-15',
      status: 'active'
    },
    {
      id: '3',
      username: 'janedoe',
      firstName: 'Jane',
      lastName: 'Doe',
      dob: '1992-08-20',
      createdDate: '2024-02-01',
      status: 'inactive'
    },
    {
      id: '4',
      username: 'bobsmith',
      firstName: 'Bob',
      lastName: 'Smith',
      dob: '1988-12-10',
      createdDate: '2024-02-15',
      status: 'active'
    }
  ];

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      // Sử dụng real API
      const response = await userAPI.getUsers();
      setUsers(response.data);
    } catch (error) {
      console.error('Error fetching users:', error);
      // Fallback to mock data nếu API fail
      setUsers(mockUsers);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setEditingUser(null);
    setModalVisible(true);
    form.resetFields();
  };

  const handleEdit = (user) => {
    setEditingUser(user);
    setModalVisible(true);
    form.setFieldsValue({
      ...user,
      dob: user.dob ? dayjs(user.dob) : null,
    });
  };

  const handleDelete = async (userId) => {
    try {
      await userAPI.deleteUser(userId);
      
      // Refresh danh sách sau khi xóa
      await fetchUsers();
    } catch (error) {
      console.error('Error deleting user:', error);
      // Error message đã được handle trong userAPI
    }
  };

  const handleSubmit = async (values) => {
    try {
      const userData = {
        ...values,
        dob: values.dob ? values.dob.format('YYYY-MM-DD') : null,
      };

      if (editingUser) {
        await userAPI.updateUser(editingUser.id, userData);
      } else {
        await userAPI.createUser(userData);
      }

      // Refresh danh sách và đóng modal
      await fetchUsers();
      setModalVisible(false);
      form.resetFields();
    } catch (error) {
      console.error('Error saving user:', error);
      // Error message đã được handle trong userAPI
    }
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
    },
    {
      title: 'Tên đăng nhập',
      dataIndex: 'username',
      key: 'username',
      render: (text) => (
        <Space>
          <UserOutlined />
          <Text strong>{text}</Text>
        </Space>
      ),
    },
    {
      title: 'Họ và tên',
      key: 'fullName',
      render: (_, record) => (
        <Text>{record.firstName} {record.lastName}</Text>
      ),
    },
    {
      title: 'Ngày sinh',
      dataIndex: 'dob',
      key: 'dob',
      render: (date) => date ? dayjs(date).format('DD/MM/YYYY') : '-',
    },
    {
      title: 'Ngày tạo',
      dataIndex: 'createdDate',
      key: 'createdDate',
      render: (date) => date ? dayjs(date).format('DD/MM/YYYY') : '-',
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag color={status === 'active' ? 'green' : 'red'}>
          {status === 'active' ? 'Hoạt động' : 'Không hoạt động'}
        </Tag>
      ),
    },
    {
      title: 'Thao tác',
      key: 'actions',
      width: 150,
      render: (_, record) => (
        <Space>
          <Tooltip title="Chỉnh sửa">
            <Button
              type="link"
              icon={<EditOutlined />}
              onClick={() => handleEdit(record)}
              size="small"
            />
          </Tooltip>
          <Tooltip title="Xóa">
            <Popconfirm
              title="Bạn có chắc chắn muốn xóa người dùng này?"
              onConfirm={() => handleDelete(record.id)}
              okText="Có"
              cancelText="Không"
            >
              <Button
                type="link"
                danger
                icon={<DeleteOutlined />}
                size="small"
              />
            </Popconfirm>
          </Tooltip>
        </Space>
      ),
    },
  ];

  const filteredUsers = users.filter(user =>
    user.username.toLowerCase().includes(searchText.toLowerCase()) ||
    user.firstName.toLowerCase().includes(searchText.toLowerCase()) ||
    user.lastName.toLowerCase().includes(searchText.toLowerCase())
  );

  return (
    <div>
      <div style={{ marginBottom: 24 }}>
        <Title level={2}>Quản lý người dùng</Title>
        <Text type="secondary">
          Quản lý thông tin và quyền hạn của người dùng trong hệ thống
        </Text>
      </div>

      <Card>
        <Row gutter={[16, 16]} style={{ marginBottom: 16 }}>
          <Col xs={24} sm={12} md={8}>
            <Search
              placeholder="Tìm kiếm theo tên hoặc username"
              allowClear
              onSearch={setSearchText}
              onChange={(e) => setSearchText(e.target.value)}
              prefix={<SearchOutlined />}
            />
          </Col>
          <Col xs={24} sm={12} md={16} style={{ textAlign: 'right' }}>
            <Space>
              <Button
                icon={<ReloadOutlined />}
                onClick={fetchUsers}
                loading={loading}
              >
                Làm mới
              </Button>
              <Button
                type="primary"
                icon={<PlusOutlined />}
                onClick={handleAdd}
              >
                Thêm người dùng
              </Button>
            </Space>
          </Col>
        </Row>

        <Table
          columns={columns}
          dataSource={filteredUsers}
          rowKey="id"
          loading={loading}
          pagination={{
            total: filteredUsers.length,
            pageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) =>
              `${range[0]}-${range[1]} của ${total} người dùng`,
          }}
          scroll={{ x: 800 }}
        />
      </Card>

      <Modal
        title={editingUser ? 'Chỉnh sửa người dùng' : 'Thêm người dùng mới'}
        open={modalVisible}
        onCancel={() => {
          setModalVisible(false);
          form.resetFields();
        }}
        footer={null}
        width={600}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          autoComplete="off"
        >
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                name="username"
                label="Tên đăng nhập"
                rules={[
                  { required: true, message: 'Vui lòng nhập tên đăng nhập!' },
                  { min: 3, message: 'Tên đăng nhập phải có ít nhất 3 ký tự!' },
                ]}
              >
                <Input placeholder="Nhập tên đăng nhập" />
              </Form.Item>
            </Col>
            <Col span={12}>
              {!editingUser && (
                <Form.Item
                  name="password"
                  label="Mật khẩu"
                  rules={[
                    { required: true, message: 'Vui lòng nhập mật khẩu!' },
                    { min: 8, message: 'Mật khẩu phải có ít nhất 8 ký tự!' },
                  ]}
                >
                  <Input.Password placeholder="Nhập mật khẩu" />
                </Form.Item>
              )}
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                name="firstName"
                label="Tên"
                rules={[{ required: true, message: 'Vui lòng nhập tên!' }]}
              >
                <Input placeholder="Nhập tên" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                name="lastName"
                label="Họ"
                rules={[{ required: true, message: 'Vui lòng nhập họ!' }]}
              >
                <Input placeholder="Nhập họ" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item
            name="dob"
            label="Ngày sinh"
          >
            <DatePicker
              style={{ width: '100%' }}
              placeholder="Chọn ngày sinh"
              format="DD/MM/YYYY"
            />
          </Form.Item>

          <Form.Item style={{ marginBottom: 0, textAlign: 'right' }}>
            <Space>
              <Button onClick={() => setModalVisible(false)}>
                Hủy
              </Button>
              <Button type="primary" htmlType="submit">
                {editingUser ? 'Cập nhật' : 'Thêm mới'}
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default Users;
