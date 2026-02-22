import axios from "../../axios"

export const fetchPosts = async () => {
    const res = await axios.get(`/posts`);
    return res.data
}

export const addPost = async (data: any) => {
    const res = await axios.post(`/posts`, data, { headers: { 'Content-Type': 'multipart/form-data' } });
    return res.data
}

export const fetchSinglePost = async (id: any) => {
    const res = await axios.get(`/posts/${id}`);
    return res.data
}

export const updatePost = async ({ postId, data }: any) => {
    const res = await axios.put(`/posts/${postId}`, data);
    return res.data
}

export const likePost = async (postId: any) => {
    const res = await axios.post(`/posts/${postId}/like`);
    return res.data
}

export const unlikePost = async (postId: any) => {
    const res = await axios.delete(`/posts/${postId}/like`);
    return res.data
}

export const addComment = async ({ postId, data }: any) => {
    const res = await axios.post(`/posts/${postId}/comments`, data);
    return res.data
}
