import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchInterestedUsers } from '../api/games'

const useInterestedUsersQuery = (gameId: string) => {
    return useQuery({
        queryKey: [QUERY_KEY.GAMES, gameId, 'interested-users'],
        queryFn: () => fetchInterestedUsers(gameId)
    })
}

export default useInterestedUsersQuery
