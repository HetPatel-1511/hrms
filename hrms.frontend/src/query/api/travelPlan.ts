import { useDispatch } from "react-redux";
import axios from "../../axios"

export const addTravelPlan = async (data:any) => {
    const res = await axios.post("/travel-plans", data);
    return res.data
}