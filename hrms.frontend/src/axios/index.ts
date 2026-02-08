import axios from "axios"

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000
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

instance.interceptors.response.use(
  // TODO: Handle errors
  (response) => {
    return response;
  },
  (error) => {
    return Promise.reject(error);
  },
);

export default axios;