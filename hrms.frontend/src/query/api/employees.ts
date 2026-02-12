import axios from "../../axios"

export const fetchEmployees = async () => {
    const res = await axios.get("/employees");
    return res.data
}