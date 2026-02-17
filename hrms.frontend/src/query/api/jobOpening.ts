import axios from "../../axios"

export const addJobOpening = async (data: any) => {
    const res = await axios.post("/job-openings", data, { headers: { 'Content-Type': 'multipart/form-data' } });
    return res.data
}

export const fetchJobOpenings = async () => {
    const res = await axios.get("/job-openings");
    return res.data
}

export const fetchSingleJobOpening = async (id: any) => {
    const res = await axios.get(`/job-openings/${id}`);
    return res.data
}

export const shareJob = async ({ jobId, data }: any) => {
    const res = await axios.post(`/job-openings/${jobId}/share`, data);
    return res.data
}

export const referFriend = async ({ jobId, data }: any) => {
    const res = await axios.post(`/job-openings/${jobId}/refer`, data, { headers: { 'Content-Type': 'multipart/form-data' } });
    return res.data
}
