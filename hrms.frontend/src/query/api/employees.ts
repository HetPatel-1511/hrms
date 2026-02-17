import axios from "../../axios"

export const fetchEmployees = async () => {
    const res = await axios.get("/employees");
    return res.data
}

export const fetchEmployeesByRole = async (roleName: string) => {
    const res = await axios.get(`/employees/role/${roleName}`);
    return res.data
}

export const fetchSingleEmployee = async (employeeId: any) => {
    const res = await axios.get(`/employees/${employeeId}/details`);
    return res.data
}