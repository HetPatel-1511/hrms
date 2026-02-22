import axios from "../../axios"

export const fetchGames = async () => {
    const res = await axios.get("/games");
    return res.data
}

export const fetchGameById = async (gameId: string) => {
    const res = await axios.get(`/games/${gameId}`);
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
