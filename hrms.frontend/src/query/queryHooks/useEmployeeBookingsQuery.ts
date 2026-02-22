import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchEmployeeBookings } from '../api/games'

const useEmployeeBookingsQuery = (employeeId: string) => {
    return useQuery({
        queryKey: [QUERY_KEY.GAMES, 'employee', employeeId, 'bookings'],
        queryFn: () => fetchEmployeeBookings(employeeId)
    })
}

export default useEmployeeBookingsQuery
