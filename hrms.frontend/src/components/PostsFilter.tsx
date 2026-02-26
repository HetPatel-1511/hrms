import Select from 'react-select'
import { XMarkIcon } from '@heroicons/react/24/solid'

interface PostsFilterProps {
    filters: {
        authorId: string
        startDate: string
        endDate: string
        searchQuery: string
        tagName: string
    }
    searchInput: string
    selectedAuthor: any
    employees: any[]
    employeesLoading: boolean
    onSearchChange: (e: React.ChangeEvent<HTMLInputElement>) => void
    onAuthorChange: (option: any) => void
    onDateChange: (type: 'startDate' | 'endDate', value: string) => void
    onTagClear: () => void
    onClearFilters: () => void
}

const PostsFilter = ({
    filters,
    searchInput,
    selectedAuthor,
    employees,
    employeesLoading,
    onSearchChange,
    onAuthorChange,
    onDateChange,
    onTagClear,
    onClearFilters
}: PostsFilterProps) => {
    const employeeOptions = employees.map((emp: any) => ({
        value: emp.id,
        label: emp.name
    }))

    const hasActiveFilters = Object.values(filters).some(v => v)

    return (
        <div className='mt-6 bg-gray-50 p-4 rounded-lg border border-gray-200'>
            <div className='mb-4'>
                <h3 className='text-lg font-semibold mb-4'>Filters</h3>
                
                <div className='grid grid-cols-1 md:grid-cols-4 gap-3 mb-4 items-end'>
                    <div>
                        <label className='block text-sm font-medium text-gray-700 mb-2'>Filter by Author</label>
                        <Select
                            isClearable
                            isLoading={employeesLoading}
                            options={employeeOptions}
                            value={selectedAuthor}
                            onChange={onAuthorChange}
                            placeholder="Select an author..."
                            classNamePrefix="react-select"
                            styles={{
                                control: (base) => ({
                                    ...base,
                                    borderColor: '#d1d5db',
                                    boxShadow: 'none',
                                    '&:hover': { borderColor: '#9ca3af' }
                                })
                            }}
                        />
                    </div>

                    <div>
                        <label className='block text-sm font-medium text-gray-700 mb-2'>Search</label>
                        <input
                            type="text"
                            placeholder="Search title, message, or author..."
                            value={searchInput}
                            onChange={onSearchChange}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    <div>
                        <label className='block text-sm font-medium text-gray-700 mb-2'>Start Date</label>
                        <input
                            type="date"
                            value={filters.startDate}
                            onChange={(e) => onDateChange('startDate', e.target.value)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    <div>
                        <label className='block text-sm font-medium text-gray-700 mb-2'>End Date</label>
                        <input
                            type="date"
                            value={filters.endDate}
                            onChange={(e) => onDateChange('endDate', e.target.value)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                </div>

                {filters.tagName && (
                    <div className='mb-4 flex items-center gap-2'>
                        <span className='text-sm font-medium text-gray-700'>Filtering by tag:</span>
                        <div className='flex items-center gap-2 px-3 py-1 bg-blue-100 rounded-full'>
                            <span className='text-sm text-blue-700'>{filters.tagName}</span>
                            <button
                                onClick={onTagClear}
                                className='text-blue-700 hover:text-blue-900 font-bold cursor-pointer'
                                aria-label="Clear tag filter"
                            >
                                <XMarkIcon className="h-4 w-4" />
                            </button>
                        </div>
                    </div>
                )}

                {hasActiveFilters && (
                    <button
                        onClick={onClearFilters}
                        className='px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition'
                    >
                        Clear Filters
                    </button>
                )}
            </div>
        </div>
    )
}

export default PostsFilter
