import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchGameById } from '../api/games'

const useSingleGameQuery = (gameId: string) => {
    return useQuery({
        queryKey: [QUERY_KEY.GAMES, gameId],
        queryFn: () => fetchGameById(gameId)
    })
}

export default useSingleGameQuery
