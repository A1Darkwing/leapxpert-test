import http from 'k6/http';
import {check} from 'k6';

// Hàm để tạo ID ngẫu nhiên
function generateId() {
    return Math.random().toString(36).substring(2, 15); // Tạo ID ngẫu nhiên
}

// Hàm để tạo số điện thoại ngẫu nhiên
function generatePhoneNumber() {
    const randomDigits = Math.floor(Math.random() * 10000000000).toString(); // Tạo số từ 0 đến 10 tỷ
    return '841' + randomDigits; // Thêm mã quốc gia
}

// Hàm để tạo type ngẫu nhiên
function getRandomReceiverType() {
    return Math.random() < 0.5 ? "INDIVIDUAL" : "GROUP"; // 50% xác suất cho mỗi loại
}

// Thiết lập thông số load test
export let options = {
    vus: 50,  // Số lượng virtual users (VUs)
    duration: '5s',  // Thời gian chạy load test
};

export default function () {
    // Body JSON để gửi yêu cầu
    const payload = JSON.stringify({
        sender: {
            phoneNumber: generatePhoneNumber(), // Tạo số điện thoại ngẫu nhiên
            displayName: "John"
        },
        receiver: {
            type: getRandomReceiverType(), // Tạo type ngẫu nhiên
            id: generatePhoneNumber() // Tạo ID ngẫu nhiên cho receiver
        },
        message: {
            id: generateId(), // Tạo ID ngẫu nhiên cho message
            timestamp: Math.floor(Date.now() / 1000), // Lấy timestamp hiện tại
            conversation: "Send you all documents!",
            attachments: [
                {
                    url: "https://secure.server/myImage.png",
                    contentType: "image/png",
                    name: "myImage.png",
                    size: 1024000
                },
                {
                    url: "https://secure.server/myDocument.pdf",
                    contentType: "application/pdf",
                    name: "myDocument.pdf",
                    size: 1024000
                }
            ]
        }
    });

    // Thực hiện yêu cầu POST
    const res = http.post('http://localhost:8079/messages/import', payload, {
        headers: {'Content-Type': 'application/json'},
    });

    // Kiểm tra phản hồi
    check(res, {
        'is status 200': (r) => r.status === 200,
        'response time < 200ms': (r) => r.timings.duration < 200,
    });
}
