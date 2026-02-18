import React from 'react'
import ErrorScreen from './ErrorScreen'

const ServerError = () => {
    return (
        <ErrorScreen
            status={"500"}
            title={"Server Error"}
            description={"Whoops, something went wrong on our servers."} />
    )
}

export default ServerError
