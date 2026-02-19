import Button from '../components/Button'
import useJobOpeningsQuery from '../query/queryHooks/useJobOpeningsQuery'
import JobOpeningItem from '../components/JobOpeningItem'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import { useAuthorization } from '../hooks/useAuthorization'

const JobOpenings = () => {
  const { data, isSuccess, isLoading, isError } = useJobOpeningsQuery()
  const { hasRole } = useAuthorization()

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
        <h1 className='text-2xl font-bold ml-4'>Job Openings</h1>
        {hasRole(["HR"]) && <div className='mt-4 ml-4'>
          <Button to={"add"}>Add Job Opening</Button>
        </div>}
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
