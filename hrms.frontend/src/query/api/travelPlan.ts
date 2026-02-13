import { useDispatch } from "react-redux";
import axios from "../../axios"

export const addTravelPlan = async (data:any) => {
    const res = await axios.post("/travel-plans", data);
    return res.data
}

export const fetchTravelPlans = async () => {
    const res = await axios.get("/travel-plans");
    return res.data
}

export const fetchSingleTravelPlans = async (id: any) => {
    const res = await axios.get(`/travel-plans/${id}`);
    return res.data
}

export const fetchEmployeeTravelPlanDocuments = async (travelPlanId: any, employeeId: any) => {
    const res = await axios.get(`/travel-plans/${travelPlanId}/employee/${employeeId}/documents`);
    return res.data
}