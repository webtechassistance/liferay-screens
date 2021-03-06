/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
#import "DDLListScreenletViewController.h"

@interface DDLListScreenletViewController ()

@property (nonatomic, weak) IBOutlet DDLListScreenlet* screenlet;

@end


@implementation DDLListScreenletViewController

- (void)viewDidLoad {
	[super viewDidLoad];

	self.screenlet.delegate = self;
	self.screenlet.userId = [[SessionContext userAttribute:@"userId"] longLongValue];
}

- (void)onDDLListResponse:(NSArray *)records {
	NSLog(@"DELEGATE onDDLListResponse -> %@", records);
}

- (void)onDDLListError:(NSError *)error {
	NSLog(@"DELEGATE onDDLListError -> %@", error);
}

- (void)onDDLRecordSelected:(DDLRecord *)record {
	NSLog(@"DELEGATE onDDLRecordSelected -> %@", record);
}

@end
