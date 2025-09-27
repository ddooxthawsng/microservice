import React from 'react';
import { Card, Row, Col, Statistic, Typography, Progress, List, Avatar } from 'antd';
import {
  UserOutlined,
  TeamOutlined,
  DashboardOutlined,
  TrophyOutlined,
  ArrowUpOutlined,
  ArrowDownOutlined
} from '@ant-design/icons';
import { useAuth } from '../../contexts/AuthContext';

const { Title, Text } = Typography;

const Dashboard = () => {
  const { user } = useAuth();

  // Mock data cho dashboard
  const stats = [
    {
      title: 'Tổng số User',
      value: 1234,
      prefix: <UserOutlined />,
      suffix: 'người',
      change: 12.5,
      positive: true,
    },
    {
      title: 'Hoạt động hôm nay',
      value: 89,
      prefix: <TeamOutlined />,
      suffix: 'lượt',
      change: -2.3,
      positive: false,
    },
    {
      title: 'Tỷ lệ hoạt động',
      value: 78.5,
      prefix: <DashboardOutlined />,
      suffix: '%',
      change: 5.7,
      positive: true,
    },
    {
      title: 'Điểm hiệu suất',
      value: 95,
      prefix: <TrophyOutlined />,
      suffix: 'điểm',
      change: 1.2,
      positive: true,
    },
  ];

  const recentActivities = [
    {
      title: 'Người dùng mới đăng ký',
      description: 'Nguyễn Văn A vừa tạo tài khoản',
      time: '2 phút trước',
      avatar: <UserOutlined />,
    },
    {
      title: 'Cập nhật thông tin',
      description: 'Trần Thị B đã cập nhật profile',
      time: '5 phút trước',
      avatar: <UserOutlined />,
    },
    {
      title: 'Đăng nhập hệ thống',
      description: 'Lê Văn C đăng nhập từ thiết bị mới',
      time: '10 phút trước',
      avatar: <UserOutlined />,
    },
    {
      title: 'Xóa tài khoản',
      description: 'Phạm Thị D đã xóa tài khoản',
      time: '15 phút trước',
      avatar: <UserOutlined />,
    },
  ];

  const performanceData = [
    { name: 'Đăng nhập', value: 85, color: '#52c41a' },
    { name: 'Đăng ký', value: 65, color: '#1890ff' },
    { name: 'Cập nhật', value: 78, color: '#faad14' },
    { name: 'Hoạt động', value: 92, color: '#722ed1' },
  ];

  return (
    <div>
      <div style={{ marginBottom: 24 }}>
        <Title level={2}>Dashboard</Title>
        <Text type="secondary">
          Chào mừng {user?.firstName} {user?.lastName} quay trở lại!
        </Text>
      </div>

      {/* Statistics Cards */}
      <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
        {stats.map((stat, index) => (
          <Col xs={24} sm={12} lg={6} key={index}>
            <Card>
              <Statistic
                title={stat.title}
                value={stat.value}
                prefix={stat.prefix}
                suffix={stat.suffix}
                valueStyle={{
                  color: stat.positive ? '#3f8600' : '#cf1322',
                }}
              />
              <div style={{ marginTop: 8 }}>
                <Text
                  type={stat.positive ? 'success' : 'danger'}
                  style={{ fontSize: 12 }}
                >
                  {stat.positive ? <ArrowUpOutlined /> : <ArrowDownOutlined />}
                  {Math.abs(stat.change)}%
                </Text>
                <Text type="secondary" style={{ marginLeft: 8, fontSize: 12 }}>
                  So với tháng trước
                </Text>
              </div>
            </Card>
          </Col>
        ))}
      </Row>

      <Row gutter={[16, 16]}>
        {/* Performance Chart */}
        <Col xs={24} lg={12}>
          <Card title="Hiệu suất hệ thống" extra={<a href="#">Xem thêm</a>}>
            {performanceData.map((item, index) => (
              <div key={index} style={{ marginBottom: 16 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4 }}>
                  <Text>{item.name}</Text>
                  <Text strong>{item.value}%</Text>
                </div>
                <Progress
                  percent={item.value}
                  strokeColor={item.color}
                  showInfo={false}
                />
              </div>
            ))}
          </Card>
        </Col>

        {/* Recent Activities */}
        <Col xs={24} lg={12}>
          <Card title="Hoạt động gần đây" extra={<a href="#">Xem tất cả</a>}>
            <List
              itemLayout="horizontal"
              dataSource={recentActivities}
              renderItem={(item) => (
                <List.Item>
                  <List.Item.Meta
                    avatar={
                      <Avatar style={{ backgroundColor: '#1890ff' }}>
                        {item.avatar}
                      </Avatar>
                    }
                    title={<Text strong>{item.title}</Text>}
                    description={
                      <div>
                        <div>{item.description}</div>
                        <Text type="secondary" style={{ fontSize: 12 }}>
                          {item.time}
                        </Text>
                      </div>
                    }
                  />
                </List.Item>
              )}
            />
          </Card>
        </Col>
      </Row>

      {/* Quick Actions */}
      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col span={24}>
          <Card title="Thao tác nhanh">
            <Row gutter={[16, 16]}>
              <Col xs={24} sm={8}>
                <Card
                  hoverable
                  style={{ textAlign: 'center', cursor: 'pointer' }}
                  onClick={() => window.location.href = '/users'}
                >
                  <UserOutlined style={{ fontSize: 32, color: '#1890ff', marginBottom: 16 }} />
                  <Title level={4}>Quản lý User</Title>
                  <Text type="secondary">Thêm, sửa, xóa người dùng</Text>
                </Card>
              </Col>
              <Col xs={24} sm={8}>
                <Card
                  hoverable
                  style={{ textAlign: 'center', cursor: 'pointer' }}
                >
                  <DashboardOutlined style={{ fontSize: 32, color: '#52c41a', marginBottom: 16 }} />
                  <Title level={4}>Báo cáo</Title>
                  <Text type="secondary">Xem báo cáo chi tiết</Text>
                </Card>
              </Col>
              <Col xs={24} sm={8}>
                <Card
                  hoverable
                  style={{ textAlign: 'center', cursor: 'pointer' }}
                >
                  <TrophyOutlined style={{ fontSize: 32, color: '#faad14', marginBottom: 16 }} />
                  <Title level={4}>Cài đặt</Title>
                  <Text type="secondary">Cấu hình hệ thống</Text>
                </Card>
              </Col>
            </Row>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;
