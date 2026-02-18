import axios from "axios"
import { logout } from "../redux/slices/userSlice";
import { store } from "../redux/store";

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 60000,
  withCredentials: true
});

instance.interceptors.request.use(
  // TODO: Add auth token
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

let isRefreshing = false;

instance.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    
    if (error.response?.status === 401 && !originalRequest._retry && !isRefreshing) {
      originalRequest._retry = true;
      isRefreshing = true;
      
      try {
        await instance.post('/auth/refresh-token');
        isRefreshing = false;
        return instance(originalRequest);
      } catch (refreshError) {
        isRefreshing = false;
        store.dispatch(logout())
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  },
);

export default instance;