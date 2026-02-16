import axios from "../../axios"

export const fetchConfigurations = async () => {
    const res = await axios.get("/configurations");
    return res.data
}

export const addConfiguration = async (data: any) => {
    const res = await axios.post("/configurations", data);
    return res.data
}

export const editConfiguration = async (data: any) => {
    const res = await axios.put(`/configurations/${data.configKey}`, data);
    return res.data
}

export const fetchSingleConfigurations = async (configKey: any) => {
    const res = await axios.get(`/configurations/${configKey}`);
    return res.data
}
