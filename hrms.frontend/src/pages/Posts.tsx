import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router'
import Button from '../components/Button'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import usePostsQuery from '../query/queryHooks/usePostsQuery'
import PostItem from '../components/PostItem'
import useEmployeesQuery from '../query/queryHooks/useEmployeeQuery'
import PostsFilter from '../components/PostsFilter'

const Posts = () => {
    const [searchParams, setSearchParams] = useSearchParams()
    const [filters, setFilters] = useState({
        authorId: searchParams.get('authorId') || '',
        startDate: searchParams.get('startDate') || '',
        endDate: searchParams.get('endDate') || '',
        searchQuery: searchParams.get('searchQuery') || '',
        tagName: searchParams.get('tagName') || ''
    })

    const [searchInput, setSearchInput] = useState(filters.searchQuery)
    const [selectedAuthor, setSelectedAuthor] = useState<any>(null)
    const { data: employeesData, isLoading: employeesLoading } = useEmployeesQuery()
    const { data, isLoading, isSuccess, isError } = usePostsQuery(
        Object.values(filters).some(v => v) ? filters : undefined
    )

    const employees = employeesData?.data || []

    // Debounce search input
    useEffect(() => {
        const timer = setTimeout(() => {
            setFilters(prev => ({
                ...prev,
                searchQuery: searchInput
            }))
        }, 500)

        return () => clearTimeout(timer)
    }, [searchInput])

    useEffect(() => {
        const params = new URLSearchParams()
        if (filters.authorId) params.append('authorId', filters.authorId)
        if (filters.startDate) params.append('startDate', filters.startDate)
        if (filters.endDate) params.append('endDate', filters.endDate)
        if (filters.searchQuery) params.append('searchQuery', filters.searchQuery)
        if (filters.tagName) params.append('tagName', filters.tagName)
        setSearchParams(params)
    }, [filters, setSearchParams])

    useEffect(() => {
        if (filters.authorId && employees.length > 0) {
            const author = employees.find((e: any) => e.id.toString() === filters.authorId)
            setSelectedAuthor(author ? { value: author.id, label: author.name } : null)
        }
    }, [filters.authorId, employees])

    const handleAuthorChange = (option: any) => {
        setFilters(prev => ({
            ...prev,
            authorId: option ? option.value.toString() : ''
        }))
        setSelectedAuthor(option)
    }

    const handleDateChange = (type: 'startDate' | 'endDate', value: string) => {
        setFilters(prev => ({
            ...prev,
            [type]: value
        }))
    }

    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchInput(e.target.value)
    }

    const handleTagClick = (tagName: string) => {
        setFilters(prev => ({
            ...prev,
            tagName: tagName
        }))
    }

    const handleClearFilters = () => {
        setSearchInput('')
        setFilters({
            authorId: '',
            startDate: '',
            endDate: '',
            searchQuery: '',
            tagName: ''
        })
        setSelectedAuthor(null)
        setSearchParams({})
    }

    if (isError) return <ServerError />

    const posts = data?.data || []

    return (
        <div>
            <h1 className='text-2xl font-bold mb-4'>Posts</h1>
            <Button to={"add"}>Add</Button>
            
            <PostsFilter
                filters={filters}
                searchInput={searchInput}
                selectedAuthor={selectedAuthor}
                employees={employees}
                employeesLoading={employeesLoading}
                onSearchChange={handleSearchChange}
                onAuthorChange={handleAuthorChange}
                onDateChange={handleDateChange}
                onTagClear={() => setFilters(prev => ({ ...prev, tagName: '' }))}
                onClearFilters={handleClearFilters}
            />

            <div className='mt-6'>
                {isLoading ? (
                    <Loading />
                ) : posts && posts.length > 0 ? (
                    posts.map((post: any) => (
                        <PostItem
                            key={post.id}
                            post={post}
                            onTagClick={handleTagClick}
                        />
                    ))
                ) : (
                    <h1 className='text-xl font-medium'>No Posts Found</h1>
                )}
            </div>
        </div>
    )
}

export default Posts
