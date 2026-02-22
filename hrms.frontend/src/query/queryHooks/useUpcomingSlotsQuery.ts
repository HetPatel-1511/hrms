import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchUpcomingSlots } from '../api/games'

const useUpcomingSlotsQuery = (gameId: string) => {
    return useQuery({
        queryKey: [QUERY_KEY.GAMES, gameId, 'upcoming-slots'],
        queryFn: () => fetchUpcomingSlots(gameId)
    })
}

export default useUpcomingSlotsQuery
