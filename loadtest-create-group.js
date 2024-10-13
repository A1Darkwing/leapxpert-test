import http from 'k6/http';
import {check} from 'k6';

// Hàm để tạo ID ngẫu nhiên
function generateId() {
    return Math.floor(Math.random() * 1000000000000).toString(); // Tạo ID từ 0 đến 1 triệu
}

// Hàm để tạo số điện thoại ngẫu nhiên
function generatePhoneNumber() {
    const randomDigits = Math.floor(Math.random() * 10000000000).toString(); // Tạo số từ 0 đến 10 tỷ
    return '846' + randomDigits; // Thêm mã quốc gia (846)
}

// Thiết lập thông số load test
export let options = {
    vus: 10,  // Số lượng virtual users (VUs)
    duration: '5s',  // Thời gian chạy load test
};

export default function () {
    // Body JSON để gửi yêu cầu
    const payload = JSON.stringify({
        name: "Group between Smith & David",
        id: generateId(),  // Gọi hàm để tạo ID ngẫu nhiên
        timestamp: Math.floor(Date.now() / 1000),  // Lấy timestamp hiện tại
        type: 2,
        members: [
            {
                phoneNumber: generatePhoneNumber(),  // Gọi hàm để tạo số điện thoại ngẫu nhiên
                name: "Smith"
            },
            {
                phoneNumber: generatePhoneNumber(),  // Gọi hàm để tạo số điện thoại ngẫu nhiên
                name: "David"
            }
        ]
    });

    // Thực hiện yêu cầu POST
    const res = http.post('http://localhost:8079/chatrooms/create', payload, {
        headers: {'Content-Type': 'application/json'},
    });

    // Kiểm tra phản hồi
    check(res, {
        'is status 200': (r) => r.status === 200,
        'response time < 200ms': (r) => r.timings.duration < 200,
    });
}
