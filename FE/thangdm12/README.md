# ThangDM Admin Dashboard

Hệ thống quản lý người dùng với xác thực và dashboard được xây dựng bằng React, Ant Design và Spring Boot.

## 🚀 Tính năng

- ✅ **Xác thực người dùng**: Đăng nhập/đăng xuất an toàn
- ✅ **Dashboard**: Trang tổng quan với thống kê và biểu đồ
- ✅ **Quản lý User**: CRUD operations cho người dùng
- ✅ **Responsive Design**: Giao diện thích ứng với mọi thiết bị
- ✅ **Protected Routes**: Bảo vệ các trang yêu cầu đăng nhập
- ✅ **Modern UI**: Sử dụng Ant Design components
- ✅ **Smart Notifications**: Hệ thống thông báo thông minh với message và notification
- ✅ **Axios Middleware**: Centralized error handling và logging

## 🛠️ Công nghệ sử dụng

### Frontend
- **React 19** - Thư viện UI
- **Ant Design 5** - Component library
- **React Router v7** - Routing
- **Axios** - HTTP client
- **Vite** - Build tool
- **Day.js** - Date utility

### Backend
- **Spring Boot 3.5.4** - Java framework
- **Spring Data JPA** - Database ORM
- **MySQL** - Database
- **MapStruct** - Object mapping
- **Lombok** - Code generation

## 📦 Cài đặt và chạy

### Frontend (thangdm12)

```bash
# Di chuyển vào thư mục frontend
cd thangdm12

# Cài đặt dependencies
npm install

# Chạy development server
npm run dev

# Build production
npm run build
```

### Backend (authen-service)

```bash
# Di chuyển vào thư mục backend
cd authen-service

# Chạy ứng dụng
mvn spring-boot:run

# Hoặc build và chạy jar
mvn clean package
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

## 🔧 Cấu hình

### Database Setup
1. Tạo database MySQL:
```sql
CREATE DATABASE auth;
```

2. Cập nhật thông tin database trong `authen-service/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: "jdbc:mysql://localhost:3306/auth?useSSL=false"
    username: root
    password: your-password
```

### API Configuration
Frontend được cấu hình để kết nối với backend API tại `http://localhost:8080/auth`

## 🎯 Cách sử dụng

### 1. Tạo user test
Trước tiên, tạo user để đăng nhập bằng cách gọi API:
```bash
curl -X POST http://localhost:8080/auth/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123", 
    "firstName": "Admin",
    "lastName": "User",
    "dob": "1990-01-01"
  }'
```

### 2. Đăng nhập
- Truy cập: `http://localhost:5173/login`
- Sử dụng tài khoản đã tạo:
  - **Username**: `admin`
  - **Password**: `admin123`

### 3. Dashboard
- Xem thống kê tổng quan
- Theo dõi hoạt động gần đây
- Truy cập nhanh các chức năng

### 4. Quản lý User
- Thêm người dùng mới
- Chỉnh sửa thông tin
- Xóa người dùng
- Tìm kiếm và lọc

## 📱 Giao diện

### Trang đăng nhập
- Form đăng nhập với validation
- Responsive design
- Thông báo lỗi thân thiện

### Dashboard
- Statistics cards
- Charts và progress bars
- Recent activities
- Quick actions

### Quản lý User
- Data table với pagination
- Search và filter
- Modal forms cho CRUD
- Confirmation dialogs

## 🔒 Bảo mật

- JWT token authentication
- Protected routes
- Automatic token refresh
- Session management
- Input validation

## 🏗️ Cấu trúc dự án

```
thangdm12/
├── src/
│   ├── components/          # Reusable components
│   │   ├── Layout/         # Layout components
│   │   ├── ProtectedRoute/ # Route protection
│   │   └── NotificationTest/ # Dev notification testing
│   ├── contexts/           # React contexts
│   ├── pages/              # Page components
│   │   ├── Login/          # Login page
│   │   ├── Dashboard/      # Dashboard page
│   │   └── Users/          # User management
│   ├── services/           # API services
│   ├── utils/              # Utility functions
│   │   ├── axiosConfig.js  # Axios middleware
│   │   └── notifications.js # Notification helpers
│   └── App.jsx             # Main app component
│
authen-service/
├── src/main/java/com/thangdm/auth_service/
│   ├── controller/         # REST controllers
│   ├── service/            # Business logic
│   ├── repository/         # Data access
│   ├── entity/             # JPA entities
│   ├── dto/                # Data transfer objects
│   ├── mapper/             # MapStruct mappers
│   └── exception/          # Exception handling
```

## 🚦 API Endpoints

### Authentication
- `POST /auth/users/login` - Đăng nhập
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```

### Users
- `GET /auth/users` - Lấy danh sách users
- `GET /auth/users/{id}` - Lấy thông tin user
- `POST /auth/users` - Tạo user mới
- `PUT /auth/users/{id}` - Cập nhật user
- `DELETE /auth/users/{id}` - Xóa user

## 🔧 Development

### Frontend Development
```bash
npm run dev     # Development server
npm run build   # Production build
npm run preview # Preview production build
npm run lint    # ESLint check
```

### Backend Development
```bash
mvn spring-boot:run     # Run application
mvn clean compile      # Compile
mvn test               # Run tests
```

## 📋 Todo List

- [x] Setup dependencies
- [x] Create API service layer
- [x] Set up authentication context
- [x] Create login page
- [x] Create dashboard
- [x] Create user management
- [x] Configure routing
- [x] Create layout components
- [x] Fix backend MapStruct

## 🤝 Đóng góp

1. Fork dự án
2. Tạo branch feature (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Liên hệ

ThangDM - [GitHub](https://github.com/thangdm)

Project Link: [https://github.com/thangdm/auth-admin-system](https://github.com/thangdm/auth-admin-system)