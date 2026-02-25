import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchUpcomingSlotsBookings } from '../api/games'

const useUpcomingSlotsBookingsQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.GAMES, 'upcoming-slots'],
        queryFn: () => fetchUpcomingSlotsBookings()
    })
}

export default useUpcomingSlotsBookingsQuery
