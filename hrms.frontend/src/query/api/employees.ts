import axios from "../../axios"

export const fetchEmployees = async (search?: string) => {
    const params = new URLSearchParams();
    if (search) {
        params.append('search', search);
    }
    const res = await axios.get(`/employees?${params.toString()}`);
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