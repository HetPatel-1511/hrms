import axios from "../../axios"

export const fetchPosts = async (filters?: any) => {
    const params = new URLSearchParams();
    if (filters?.authorId) params.append('authorId', filters.authorId);
    if (filters?.tagName) params.append('tagName', filters.tagName);
    if (filters?.startDate) params.append('startDate', filters.startDate);
    if (filters?.endDate) params.append('endDate', filters.endDate);
    if (filters?.searchQuery) params.append('searchQuery', filters.searchQuery);
    
    const queryString = params.toString();
    const url = queryString ? `/posts?${queryString}` : '/posts';
    const res = await axios.get(url);
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

export const deleteComment = async ({ postId, commentId }: any) => {
    const res = await axios.delete(`/posts/${postId}/comments/${commentId}`);
    return res.data
}

export const likeComment = async ({ postId, commentId }: any) => {
    const res = await axios.post(`/posts/${postId}/comments/${commentId}/like`);
    return res.data
}

export const unlikeComment = async ({ postId, commentId }: any) => {
    const res = await axios.delete(`/posts/${postId}/comments/${commentId}/like`);
    return res.data
}

export const deletePost = async (postId: any) => {
    const res = await axios.delete(`/posts/${postId}`);
    return res.data
}
