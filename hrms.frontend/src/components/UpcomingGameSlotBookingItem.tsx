import Button from './Button'
import Card from './Card'
import UserAvatar from './UserAvatar'
import { Link } from 'react-router'

const UpcomingGameSlotBookingItem = ({ slotBooking }: any) => {
    const formatTime = (time: string) => {
        return new Date(`1970-01-01T${time}`).toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit'
        })
    }

    return (
        <Card className="mt-4">
            <div className="px-4 py-4 sm:px-6">
                <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg font-semibold text-indigo-600">
                        {slotBooking.game.name}
                    </h3>
                    <Button
                        to={`/games/${slotBooking.game.id}`}
                    >
                        Book
                    </Button>
                </div>

                <div className="space-y-3">
                    {slotBooking.slots.map((slot: any) => (
                        <div key={slot.slotId} className="border border-gray-200 rounded-lg p-4 bg-gray-50">
                            <div className="flex items-center justify-between">
                                <div className="flex-1">
                                    <p className="text-sm font-medium text-gray-900">
                                        {formatTime(slot.startTime)} - {formatTime(slot.endTime)}
                                    </p>
                                    <p className="text-xs text-gray-500 mt-1">
                                        Status: <span className={`inline-block px-2 py-1 rounded-full text-xs font-medium ${slot.slotStatus === 'BOOKED'
                                                ? 'bg-green-100 text-green-800'
                                                : 'bg-blue-100 text-blue-800'
                                            }`}>
                                            {slot.slotStatus}
                                        </span>
                                    </p>
                                </div>
                            </div>

                            {/* Display bookings only if slot is booked */}
                            {slot.bookings && slot.bookings.length > 0 && (
                                <div className="mt-3 pt-3 border-t border-gray-200">
                                    <p className="text-xs text-gray-600 font-medium mb-2">Booked by:</p>
                                    {slot.bookings.map((booking: any) => (
                                        <>
                                            <div key={booking.bookingId} className="flex items-center space-x-3 p-2 rounded">
                                                <UserAvatar
                                                    user={{ image: booking.bookedBy?.profileMedia?.url }}
                                                    className="h-8 w-8"
                                                />
                                                <div className="flex-1 min-w-0">
                                                    <p className="text-sm font-medium text-gray-900 truncate">
                                                        {booking.bookedBy?.name}
                                                    </p>
                                                    <p className="text-xs text-gray-500 truncate">
                                                        {booking.bookedBy?.email}
                                                    </p>
                                                </div>
                                                <span className={`inline-flex px-2 py-1 rounded-full text-xs font-medium whitespace-nowrap ${booking.bookingStatus === 'CONFIRMED'
                                                        ? 'bg-green-100 text-green-800'
                                                        : booking.bookingStatus === 'WAITING'
                                                            ? 'bg-yellow-100 text-yellow-800'
                                                            : 'bg-gray-100 text-gray-800'
                                                    }`}>
                                                    {booking.bookingStatus}
                                                </span>
                                            </div>

                                            {booking?.playedWith && booking?.playedWith.length > 0 && (
                                                <div className="mt-3 p-2 rounded">
                                                    <p className="text-xs text-gray-600 font-medium mb-2">Playing with:</p>
                                                    <div className="space-y-1">
                                                        {booking?.playedWith.map((player: any) => (
                                                            <div key={player.id} className="flex items-center space-x-2">
                                                                <UserAvatar
                                                                    user={{ image: player.profileMedia?.url }}
                                                                    className="h-6 w-6"
                                                                />
                                                                <div className="flex-1 min-w-0">
                                                                    <p className="text-xs font-medium text-gray-900 truncate">
                                                                        {player.name}
                                                                    </p>
                                                                    <p className="text-xs text-gray-500 truncate">
                                                                        {player.email}
                                                                    </p>
                                                                </div>
                                                            </div>
                                                        ))}
                                                    </div>
                                                </div>
                                            )}
                                        </>
                                    ))}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </Card>
    )
}

export default UpcomingGameSlotBookingItem
