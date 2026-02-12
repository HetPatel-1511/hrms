import { useDispatch } from "react-redux";
import axios from "../../axios"

export const login = async (data:any) => {
    const res = await axios.post("/auth/login", data);
    return res.data
}