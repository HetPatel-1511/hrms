import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchGames } from '../api/games'

const useGamesQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.GAMES],
        queryFn: () => fetchGames()
    })
}

export default useGamesQuery
