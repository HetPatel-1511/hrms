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

export const fetchEmployeeTravelPlanExpenses = async (travelPlanId: any, employeeId: any) => {
    const res = await axios.get(`/expenses/travel-plan/${travelPlanId}/employee/${employeeId}`);
    return res.data
}

export const addTravelPlanDocument = async ({travelPlanId, employeeId, data}: any) => {
    const res = await axios.post(`/travel-plans/${travelPlanId}/employee/${employeeId}/documents`, data, { headers: { 'Content-Type': 'multipart/form-data' } });
    return res.data
}

export const addTravelPlanExpense = async ({travelPlanId, employeeId, data}: any) => {
    const res = await axios.post(`expenses/travel-plan/${travelPlanId}/employee/${employeeId}`, data, { headers: { 'Content-Type': 'multipart/form-data' } });
    return res.data
}

export const changeTravelPlanExpenseStatus = async ({expenseId, data}: any) => {
    const res = await axios.put(`expenses/${expenseId}/status`, data);
    return res.data
}

export const fetchTravelPlanById = async (expenseId: any) => {
    const res = await axios.get(`expenses/${expenseId}`);
    return res.data
}