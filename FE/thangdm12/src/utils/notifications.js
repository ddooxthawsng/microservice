import { message, notification } from 'antd';

// Cấu hình mặc định cho notifications
message.config({
  top: 100,
  duration: 3,
  maxCount: 5,
});

notification.config({
  placement: 'topRight',
  top: 80,
  duration: 4,
  rtl: false,
});

// Helper functions cho notifications
export const showSuccess = (content, description = null) => {
  if (description) {
    notification.success({
      message: content,
      description,
      duration: 4,
    });
  } else {
    message.success(content);
  }
};

export const showError = (content, description = null) => {
  if (description) {
    notification.error({
      message: content,
      description,
      duration: 5,
    });
  } else {
    message.error(content);
  }
};

export const showWarning = (content, description = null) => {
  if (description) {
    notification.warning({
      message: content,
      description,
      duration: 4,
    });
  } else {
    message.warning(content);
  }
};

export const showInfo = (content, description = null) => {
  if (description) {
    notification.info({
      message: content,
      description,
      duration: 4,
    });
  } else {
    message.info(content);
  }
};

// Notification cho các hành động cụ thể
export const authNotifications = {
  loginSuccess: (userName) => {
    notification.success({
      message: 'Đăng nhập thành công!',
      description: `Chào mừng ${userName} quay trở lại hệ thống!`,
      duration: 4,
      placement: 'topRight',
    });
  },
  
  loginError: (error) => {
    notification.error({
      message: 'Đăng nhập thất bại!',
      description: error || 'Vui lòng kiểm tra lại thông tin đăng nhập!',
      duration: 5,
      placement: 'topRight',
    });
  },
  
  logoutSuccess: () => {
    message.success('Đăng xuất thành công!');
  },
  
  sessionExpired: () => {
    notification.warning({
      message: 'Phiên đăng nhập hết hạn',
      description: 'Vui lòng đăng nhập lại để tiếp tục!',
      duration: 6,
      placement: 'topRight',
    });
  }
};

export const userNotifications = {
  createSuccess: (userName) => {
    notification.success({
      message: 'Thêm người dùng thành công!',
      description: `Đã tạo tài khoản cho ${userName}`,
      duration: 4,
    });
  },
  
  updateSuccess: (userName) => {
    notification.success({
      message: 'Cập nhật thành công!',
      description: `Đã cập nhật thông tin cho ${userName}`,
      duration: 4,
    });
  },
  
  deleteSuccess: () => {
    message.success('Xóa người dùng thành công!');
  },
  
  loadError: () => {
    notification.error({
      message: 'Lỗi tải dữ liệu',
      description: 'Không thể tải danh sách người dùng!',
      duration: 4,
    });
  }
};

export const networkNotifications = {
  connectionError: () => {
    notification.error({
      message: 'Lỗi kết nối',
      description: 'Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng!',
      duration: 8,
      placement: 'topRight',
    });
  },
  
  serverError: () => {
    notification.error({
      message: 'Lỗi server',
      description: 'Server đang gặp sự cố. Vui lòng thử lại sau!',
      duration: 6,
      placement: 'topRight',
    });
  },
  
  timeout: () => {
    notification.warning({
      message: 'Timeout',
      description: 'Yêu cầu mất quá nhiều thời gian. Vui lòng thử lại!',
      duration: 5,
    });
  }
};

