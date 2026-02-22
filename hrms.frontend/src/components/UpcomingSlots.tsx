import React from "react";

const UpcomingSlots = ({ slots, selectedSlot, onSlotSelect }: any) => {
    const formatTime = (time: string) => {
        return new Date(`1970-01-01T${time}`).toLocaleTimeString('en-US', {
            hour: '2-digit',
            minute: '2-digit'
        })
    }

    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleDateString('en-US', {
            weekday: 'short',
            month: 'short',
            day: 'numeric'
        })
    }

    return (
        <div className="px-6 py-4">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Upcoming Available Slots</h2>
            <div className="space-y-4">
                {slots.map((slotGroup: any) => (
                    <div key={slotGroup.slotDate}>
                        <h3 className="font-medium text-gray-900 mb-3">
                            {formatDate(slotGroup.slotDate)}
                        </h3>
                        <div className="grid grid-cols-3 gap-2">
                            {slotGroup.slotTimes.map((slot: any) => (
                                <button
                                    key={slot.slotId}
                                    onClick={() => onSlotSelect(slot.slotId.toString())}
                                    disabled={slot.slotStatus != 'AVAILABLE'}
                                    className={`p-3 rounded-lg border text-sm cursor-pointer font-medium transition-colors ${
                                        selectedSlot == slot.slotId.toString()
                                            ? 'border-indigo-500 text-indigo-600'
                                            : 'border-gray-300 text-gray-700'
                                    }`}
                                >
                                    <div>{formatTime(slot.startTime)} - {formatTime(slot.endTime)}</div>
                                    {slot.slotStatus !== 'AVAILABLE' && (
                                        <div className="text-xs mt-1">{slot.slotStatus}</div>
                                    )}
                                </button>
                            ))}
                        </div>
                    </div>
                ))}
                {slots.length === 0 && (
                    <p className="text-gray-500 text-center py-8">No upcoming slots available</p>
                )}
            </div>
        </div>
    )
}

export default UpcomingSlots
