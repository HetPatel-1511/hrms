import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import useUpcomingSlotsBookingsQuery from '../query/queryHooks/useUpcomingSlotsBookingsQuery'
import UpcomingGameSlotBookingItem from '../components/UpcomingGameSlotBookingItem'

const Home = () => {
    const { data, isLoading, isSuccess, isError } = useUpcomingSlotsBookingsQuery()

    if (isLoading) {
        return <Loading />
    }
    if (isError) {
        return <ServerError />
    }
    if (isSuccess) {
        const upcomingSlotBookings = data?.data || []
        return (
            <div>
                <h1 className='text-2xl font-bold mb-4'>Upcoming Game Slots</h1>
                <div className='grid gap-4 grid-cols-2'>
                    {upcomingSlotBookings && upcomingSlotBookings.length > 0 ?
                        upcomingSlotBookings.map((slotBooking: any) => {
                            return (
                                <UpcomingGameSlotBookingItem key={slotBooking.game.id} slotBooking={slotBooking} />
                            )
                        }) :
                        <h1 className='text-xl font-medium'>No upcoming game slots</h1>
                    }
                </div>
            </div>
        )
    }
}

export default Home
