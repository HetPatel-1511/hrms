import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import useEmployeeBookingsQuery from '../query/queryHooks/useEmployeeBookingsQuery'
import GameBookingItem from '../components/GameBookingItem'
import { useSelector } from 'react-redux'
import { selectUser } from '../redux/slices/userSlice'

const GameBookings = () => {
    const currentUser = useSelector(selectUser)
    const { data, isLoading, isSuccess, isError } = useEmployeeBookingsQuery(currentUser?.id?.toString() || '')

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const bookings = data?.data || []
        return (
            <div>
                <h1 className='text-2xl font-bold mb-4'>My Game Bookings</h1>
                <div className='mt-6'>
                    {bookings && bookings.length > 0 ?
                        bookings.map((booking: any) => {
                            return (
                                <GameBookingItem key={booking.game.id} booking={booking} />
                            )
                        }) :
                        <h1 className='text-xl font-medium'>No Game Bookings</h1>
                    }
                </div>
            </div>
        )
    }
}

export default GameBookings
