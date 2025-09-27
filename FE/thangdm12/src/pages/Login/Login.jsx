import React, { useState } from 'react';
import { Form, Input, Button, Card, Typography, message, notification, Tabs, DatePicker } from 'antd';
import { UserOutlined, LockOutlined, MailOutlined, CalendarOutlined } from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { userAPI } from '../../services/api';
import './Login.css';

const { Title, Text } = Typography;

const Login = () => {
  const [loginLoading, setLoginLoading] = useState(false);
  const [registerLoading, setRegisterLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [loginForm] = Form.useForm();
  const [registerForm] = Form.useForm();

  const from = location.state?.from?.pathname || '/dashboard';

  const onLoginFinish = async (values) => {
    setLoginLoading(true);
    
    try {
      const result = await login(values);
      
      if (result.success) {
        // Hiển thị thông báo đăng nhập thành công
        notification.success({
          message: '🎉 Đăng nhập thành công!',
          description: 'Chào mừng bạn quay trở lại hệ thống!',
          duration: 3,
          placement: 'topRight',
        });
        
        // Delay một chút để user thấy notification trước khi chuyển trang
        setTimeout(() => {
          navigate(from, { replace: true });
        }, 800);
      }
    } catch (error) {
      // Error notification được handle tự động bởi axios interceptor
      console.error('Login error:', error);
    } finally {
      setLoginLoading(false);
    }
  };

  const onRegisterFinish = async (values) => {
    setRegisterLoading(true);
    
    try {
      const userData = {
        ...values,
        dob: values.dob ? values.dob.format('YYYY-MM-DD') : null,
      };
      
      await userAPI.createUser(userData);
      
      // Hiển thị thông báo đăng ký thành công
      notification.success({
        message: '🎊 Đăng ký thành công!',
        description: `Tài khoản ${userData.username} đã được tạo. Bạn có thể đăng nhập ngay bây giờ!`,
        duration: 4,
        placement: 'topRight',
      });
      
      // Reset form và chuyển về tab đăng nhập
      registerForm.resetFields();
      
      // Tự động fill thông tin vào form đăng nhập
      loginForm.setFieldsValue({
        username: userData.username,
        password: ''
      });
      
    } catch (error) {
      // Error notification được handle tự động bởi axios interceptor
      console.error('Register error:', error);
    } finally {
      setRegisterLoading(false);
    }
  };

  const tabItems = [
    {
      key: 'login',
      label: 'Đăng nhập',
      children: (
        <Form
          form={loginForm}
          name="login"
          className="login-form"
          onFinish={onLoginFinish}
          autoComplete="off"
          size="large"
        >
          <Form.Item
            name="username"
            rules={[
              {
                required: true,
                message: 'Vui lòng nhập tên đăng nhập!',
              },
              {
                min: 3,
                message: 'Tên đăng nhập phải có ít nhất 3 ký tự!',
              },
            ]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="Tên đăng nhập"
            />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[
              {
                required: true,
                message: 'Vui lòng nhập mật khẩu!',
              },
              {
                min: 5,
                message: 'Mật khẩu phải có ít nhất 5 ký tự!',
              },
            ]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="Mật khẩu"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="login-button"
              loading={loginLoading}
              block
            >
              Đăng nhập
            </Button>
          </Form.Item>
        </Form>
      ),
    },
    {
      key: 'register',
      label: 'Đăng ký',
      children: (
        <Form
          form={registerForm}
          name="register"
          className="login-form"
          onFinish={onRegisterFinish}
          autoComplete="off"
          size="large"
        >
          <Form.Item
            name="username"
            rules={[
              {
                required: true,
                message: 'Vui lòng nhập tên đăng nhập!',
              },
              {
                min: 3,
                message: 'Tên đăng nhập phải có ít nhất 3 ký tự!',
              },
            ]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="Tên đăng nhập"
            />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[
              {
                required: true,
                message: 'Vui lòng nhập mật khẩu!',
              },
              {
                min: 8,
                message: 'Mật khẩu phải có ít nhất 8 ký tự!',
              },
            ]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="Mật khẩu (tối thiểu 8 ký tự)"
            />
          </Form.Item>

          <Form.Item
            name="firstName"
            rules={[
              {
                required: true,
                message: 'Vui lòng nhập tên!',
              },
            ]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="Tên"
            />
          </Form.Item>

          <Form.Item
            name="lastName"
            rules={[
              {
                required: true,
                message: 'Vui lòng nhập họ!',
              },
            ]}
          >
            <Input
              prefix={<UserOutlined />}
              placeholder="Họ"
            />
          </Form.Item>

          <Form.Item
            name="dob"
            rules={[
              {
                required: false,
              },
            ]}
          >
            <DatePicker
              prefix={<CalendarOutlined />}
              placeholder="Ngày sinh (tùy chọn)"
              style={{ width: '100%' }}
              format="DD/MM/YYYY"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="login-button"
              loading={registerLoading}
              block
            >
              Đăng ký tài khoản
            </Button>
          </Form.Item>
        </Form>
      ),
    },
  ];

  return (
    <div className="login-container">
      <div className="login-form-wrapper">
        <Card className="login-card">
          <div className="login-header">
            <Title level={2} className="login-title">
              Hệ thống quản lý
            </Title>
            <Text type="secondary">
              Đăng nhập hoặc tạo tài khoản mới
            </Text>
            

          </div>
          
          <Tabs
            defaultActiveKey="login"
            items={tabItems}
            centered
            size="large"
          />
        </Card>
      </div>
    </div>
  );
};

export default Login;
