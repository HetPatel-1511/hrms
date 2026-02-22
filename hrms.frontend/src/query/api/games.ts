import axios from "../../axios"

export const fetchGames = async () => {
    const res = await axios.get("/games");
    return res.data
}

export const fetchGameById = async (gameId: string) => {
    const res = await axios.get(`/games/${gameId}`);
    return res.data
}

export const fetchUserInterestedGames = async (userId: string) => {
    const res = await axios.get(`/games/users/${userId}/interested`);
    return res.data
}

export const fetchUpcomingSlots = async (gameId: string) => {
    const res = await axios.get(`/games/${gameId}/upcoming-slots`);
    return res.data
}

export const fetchInterestedUsers = async (gameId: string) => {
    const res = await axios.get(`/games/${gameId}/interested`);
    return res.data
}

export const fetchEmployeeBookings = async (employeeId: string) => {
    const res = await axios.get(`/games/employees/${employeeId}/booking`);
    return res.data
}

export const cancelBooking = async (bookingId: number) => {
    const res = await axios.post(`/games/bookings/${bookingId}/cancel`);
    return res.data
}

export const bookGameSlot = async (data: any) => {
    const res = await axios.post("/games/book", data);
    return res.data
}

export const addGame = async (data: any) => {
    const res = await axios.post("/games", data);
    return res.data
}

export const updateGameConfiguration = async ({ gameId, data }: any) => {
    const res = await axios.put(`/games/${gameId}/configuration`, data);
    return res.data
}

export const updateGameInterest = async (gameId: string) => {
    const res = await axios.post(`/games/${gameId}/interested`);
    return res.data
}
