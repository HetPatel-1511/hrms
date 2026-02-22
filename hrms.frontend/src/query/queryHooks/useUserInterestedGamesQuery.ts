import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchUserInterestedGames } from '../api/games'

const useUserInterestedGamesQuery = (userId: string) => {
    return useQuery({
        queryKey: [QUERY_KEY.GAMES, 'user', userId, 'interested'],
        queryFn: () => fetchUserInterestedGames(userId)
    })
}

export default useUserInterestedGamesQuery
