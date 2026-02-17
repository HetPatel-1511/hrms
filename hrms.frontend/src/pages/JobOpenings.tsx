import Button from '../components/Button'
import useJobOpeningsQuery from '../query/queryHooks/useJobOpeningsQuery'
import JobOpeningItem from '../components/JobOpeningItem'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'

const JobOpenings = () => {
  const { data, isSuccess, isLoading, isError } = useJobOpeningsQuery()

  if (isLoading) {
    return <Loading />
  }
  if (isError) {
    return <ServerError />
  }
  if (isSuccess) {
    const jobOpenings = data?.data || [];
    return (
      <>
        <div className='m-4'>
          <Button to={"add"}>Add Job Opening</Button>
        </div>
        <div className="p-4">
          {jobOpenings.map((jobOpening: any) => (
            <JobOpeningItem 
              key={jobOpening.id} 
              jobOpening={jobOpening}
            />
          ))}
        </div>
      </>
    )
  }
}

export default JobOpenings
