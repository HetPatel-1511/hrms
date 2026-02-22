import Card from './Card'
import UserAvatar from './UserAvatar'
import Button from './Button'
import useCancelBookingMutation from '../query/queryHooks/useCancelBookingMutation'
import { useAuthorization } from '../hooks/useAuthorization'
import { toast } from 'react-toastify'

const GameBookingItem = ({ booking }: any) => {
    const cancelBooking = useCancelBookingMutation()
    const { isOwner } = useAuthorization()

    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleDateString('en-US', {
            weekday: 'short',
            month: 'short',
            day: 'numeric'
        })
    }

    const formatTime = (time: string) => {
        return new Date(`1970-01-01T${time}`).toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit'
        })
    }

    const isBookingInFuture = (slotDate: string, startTime: string) => {
        const bookingDateTime = new Date(`${slotDate}T${startTime}`)
        const currentDateTime = new Date()
        return bookingDateTime > currentDateTime
    }

    const handleCancelBooking = (bookingId: number) => {
        if (window.confirm('Are you sure you want to cancel this booking?')) {
            cancelBooking.mutate(bookingId, {
                onSuccess: (response) => {
                    toast.success(response.data || 'Booking cancelled successfully!')
                }
            })
        }
    }

    return (
        <Card className="mt-2">
            <div className="px-4 py-4 sm:px-6">
                <div className="flex items-center justify-between mb-3">
                    <h3 className="text-lg font-medium text-indigo-600">
                        {booking.game.name}
                    </h3>
                </div>
                
                <div className="space-y-2">
                    {booking.booking.map((dateBooking: any) => (
                        <div key={dateBooking.slotDate} className="border border-gray-200 rounded-lg p-3">
                            <h4 className="font-medium text-gray-900 mb-2">
                                {formatDate(dateBooking.slotDate)}
                            </h4>
                            <div className="space-y-2">
                                {dateBooking.slotTimes.map((slot: any) => (
                                    <div className="border-b border-gray-200 pb-4">
                                    <div key={slot.slotId} className="flex items-center justify-between py-2 rounded">
                                        <div className="flex items-center space-x-3">
                                            <div>
                                                <p className="text-sm font-medium text-gray-900">
                                                    {formatTime(slot.startTime)} - {formatTime(slot.endTime)}
                                                </p>
                                                <p className="text-xs text-gray-500">
                                                    Status: {slot.bookingStatus}
                                                </p>
                                            </div>
                                            {slot.bookedBy && (
                                                <div className="flex items-center space-x-2">
                                                    <UserAvatar 
                                                        user={{ image: slot.bookedBy.profileMedia?.url }} 
                                                        className="h-6 w-6" 
                                                    />
                                                    <div>
                                                        <p className="text-sm font-medium text-gray-900">
                                                            {slot.bookedBy.name}
                                                        </p>
                                                        <p className="text-xs text-gray-500">
                                                            {slot.bookedBy.email}
                                                        </p>
                                                    </div>
                                                </div>
                                            )}
                                        </div>
                                        <div className="flex items-center space-x-2">
                                            {slot.bookingStatus !== 'CANCELLED'
                                                && slot.bookingStatus !== 'COMPLETED'
                                                && isOwner(slot.bookedBy.id)
                                                && isBookingInFuture(dateBooking.slotDate, slot.startTime)
                                                && (
                                                    <Button
                                                        onClick={() => handleCancelBooking(slot.bookingId)}
                                                        disabled={cancelBooking.isPending}
                                                        className="text-xs px-2 py-1"
                                                    >
                                                        {cancelBooking.isPending ? 'Canceling...' : 'Cancel'}
                                                    </Button>
                                                )
                                            }
                                        </div>
                                    </div>
                                    <div>
                                        {slot.playedWith && slot.playedWith.length > 0 && (
                                                <div className="flex-1">
                                                    <p className="text-xs text-gray-500 mb-2">Played with:</p>
                                                    <div className="space-y-1">
                                                        {slot.playedWith.map((player: any) => (
                                                            <div key={player.id} className="flex items-center space-x-2 p-1 rounded">
                                                                <UserAvatar 
                                                                    user={{ image: player.profileMedia?.url }} 
                                                                    className="h-4 w-4" 
                                                                />
                                                                <div className="flex-1">
                                                                    <p className="text-xs font-medium text-gray-900">{player.name}</p>
                                                                    <p className="text-xs text-gray-500">{player.email}</p>
                                                                </div>
                                                            </div>
                                                        ))}
                                                    </div>
                                                </div>
                                            )}
                                    </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </Card>
    )
}

export default GameBookingItem
