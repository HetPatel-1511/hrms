import axios from "../../axios"

export const fetchNotifications = async () => {
    const res = await axios.get(`/notifications`);
    return res.data
}

export const fetchUnreadNotifications = async () => {
    const res = await axios.get(`/notifications/unread`);
    return res.data
}

export const fetchUnreadCount = async () => {
    const res = await axios.get(`/notifications/unread/count`);
    return res.data
}
export const markNotificationAsRead = async (notificationId: any) => {
    const res = await axios.put(`/notifications/${notificationId}/read`);
    return res.data
}