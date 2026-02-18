import ErrorScreen from '../components/ErrorScreen'
import Navbar from '../components/Navbar'

const NotFound404 = () => {
    return (
        <div>
            <Navbar />
            <div className='pt-20'>
                <ErrorScreen
                    status={"404"}
                    title={"Error"}
                    description={"Whoops, resource not found."} />
            </div>
        </div>
    )
}

export default NotFound404
